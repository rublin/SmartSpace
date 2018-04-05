package org.rublin.controller;

import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Send mobile notification (sms or calls) using modem (usb modem or mobile phone)
 *
 * @author Ruslan Sheremet
 * @see SerialPort
 * @since 1.0
 */
@Slf4j
@Component
public class ModemController {

    @Value("${modem.port}")
    private String smsPort;

    private  SerialPort serialPort;
    private  final String AT_CALL = "ATD";
    private  final String AT_ANSWER_CALL = "ATA\r";
    private  final String AT_END_CALL = "AT+CHUP\r";
    private  final String AT_DIGITAL_MODE = "AT+CMGF=0";
    private  final String AT_TEXT_MODE = "AT+CMGF=1\r";
    private  final String AT_SMS = "AT+CMGS=";
    private  final String AT_READ_ALL_SMS = "AT+CMGL=\"ALL\"\r";
    private  final String AT_READ_SMS = "AT+CMGR=";
    private  final int CALL_END_TIME = 500;

    /**
     * Make a call to phone number
     *
     * @param number phone number
     * @return response string or null if exception happens
     */
    public synchronized String call(String number)  {
        try {
            serialPort.writeBytes((AT_CALL + number + ";\r").getBytes());
//            System.out.println("call start");
            log.info("Call to {} send", number);
            waitSerialResponse();
            return serialPort.readString();
        } catch (SerialPortException e) {
            log.error("Unexpected error: {}", e);
        }
        return null;
    }

    /**
     * Make a call to phone number with a timeout throw Thread.sleep
     * @see Thread
     * @param number phone number
     * @param timeout millis
     * @return response string or null if exception happens
     */
    public String call(String number, int timeout) {
        String response = call(number);
        log.info(response);
        if (response != null) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            log.info("Try to end call to {}", number);
            try {
                serialPort.writeBytes(AT_END_CALL.getBytes());
                waitSerialResponse(CALL_END_TIME);
                return serialPort.readString();
            } catch (SerialPortException e) {
                log.error("Unexpected error: {}", e);
            }
        }
        return null;
    }

    /**
     * Answer incoming call
     * @return response string
     * @throws SerialPortException
     */
    public String answerCall() throws SerialPortException {
        serialPort.writeBytes(AT_ANSWER_CALL.getBytes());
        waitSerialResponse();
        return serialPort.readString();
    }

    /**
     * Send sms with message to the number
     *
     * @param number mobile number
     * @param message sms text message
     * @return response string or null if exception happens
     * @throws SerialPortException
     */
    public String sendSms (String number, String message)  {
        try {
            serialPort.writeBytes(AT_TEXT_MODE.getBytes());
            serialPort.writeBytes(String.format("%s\"%s\"\r", AT_SMS, number).getBytes());
            serialPort.writeBytes(String.format("%s\032\r", message).getBytes());
            log.info("Send sms to {} success", number);
            waitSerialResponse();
            return serialPort.readString();
        } catch (SerialPortException e) {
            log.error("Unexpected error: {}", e);
        }
        return null;
    }

    /**
     * Read incoming sms messages, delete and return them in PDU format
     *
     * @return {@link List} of sms messages in PDU format
     */
    public List<String> readSms() {
        try {
            serialPort.writeBytes(AT_DIGITAL_MODE.getBytes());
            List<String> lines = new ArrayList<>();
            for (int i = 20; i >= 0; i--) {
                serialPort.writeBytes(String.format("%s%d%s", AT_READ_SMS, i, "\r").getBytes());
                waitSerialResponse();
                String receivedLines = serialPort.readString();
                if (receivedLines.contains("+CMGR")) {
                    String message = toPDU(receivedLines);
                    log.info("SMS message with id {} read success. Message is {}", i, message);
                    lines.add(toPDU(receivedLines));
                    deleteSms(i);
                }
            }
            return lines;
        } catch (SerialPortException e) {
            log.error("Unexpected error: {}", e);
            return null;
        }
    }

    /**
     * Delete sms message by id
     * @param id id
     * @throws SerialPortException
     */
    public void deleteSms(int id) throws SerialPortException {
        serialPort.writeBytes(String.format("AT+CMGD=%d\r", id).getBytes());
        waitSerialResponse();
        log.info("Sms with id {} deleted", id);
    }

    /**
     * Start method uses to start communicate with USB modem
     */
    public void start() {
        try {
            serialPort = new SerialPort(smsPort);
            serialPort.openPort ();
            serialPort.setParams (SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.writeBytes("AT\r".getBytes());
        } catch (SerialPortException e) {
            log.error("Unexpected error: {}", e);
        }
    }

    /**
     * Stop method uses to stom communicate with USB modem
     */
    public void stop() {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            log.error("Unexpected error: {}", e);
        }
    }

    /**
     * Find PDU string and return a {@link List} of PDU strings
     *
     * @param request
     * @return {@link List} of PDU strings
     */
    private String toPDU(String request) {
        List<String> lines = Arrays.asList(request.split("\r\n"));
        return lines.stream()
                .filter(line -> line.startsWith("0"))
                .collect(Collectors.joining());
    }

    /**
     * Wait default timeout (10 millis)
     */
    private void waitSerialResponse() {
        waitSerialResponse(10);
    }

    /**
     * Wait custom timeout
     *
     * @param timeout
     */
    private void waitSerialResponse(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            log.error("Unexpected error: {}", e);
        }
    }
}