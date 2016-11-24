package org.rublin.controller;

import jssc.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.rublin.util.Resources.*;
import static org.slf4j.LoggerFactory.getLogger;


/**
 * Send mobile notification (sms or calls) using modem (usb modem or mobile phone
 */
public class ModemController {

    private static final Logger LOG = getLogger(ModemController.class);

    private static SerialPort serialPort;
    private static final String AT_CALL = "ATD";
    private static final String AT_ANSWER_CALL = "ATA\r";
    private static final String AT_END_CALL = "AT+CHUP\r";
    private static final String AT_DIGITAL_MODE = "AT+CMGF=0";
    private static final String AT_TEXT_MODE = "AT+CMGF=1\r";
    private static final String AT_SMS = "AT+CMGS=";
    private static final String AT_READ_ALL_SMS = "AT+CMGL=\"ALL\"\r";
    private static final String AT_READ_SMS = "AT+CMGR=";

    /**
     * Make a call to phone number
     *
     * @param number phone number
     * @return response string or null if exception happens
     */
    public String call(String number)  {
        try {
            serialPort.writeBytes((AT_CALL + number + ";\r").getBytes());
//            System.out.println("call start");
            LOG.info("Call to {} send", number);
            waitSerialResponse();
            return serialPort.readString();
        } catch (SerialPortException e) {
            LOG.error(e.getMessage());
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
        LOG.info(response);
        if (response != null) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                LOG.error(e.getMessage());
            }
            LOG.info("Try to end call to {}", number);
            try {
                serialPort.writeBytes(AT_END_CALL.getBytes());
                waitSerialResponse();
                return serialPort.readString();
            } catch (SerialPortException e) {
                LOG.error("Unexpected error: {}", e);
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
            LOG.info("Send sms to {} success", number);
            waitSerialResponse();
            return serialPort.readString();
        } catch (SerialPortException e) {
            LOG.error("Unexpected error: {}", e);
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
                    LOG.info("SMS message with id {} read success. Message is {}", i, message);
                    lines.add(toPDU(receivedLines));
                    deleteSms(i);
                }
            }
            return lines;
        } catch (SerialPortException e) {
            LOG.error("Unexpected error: {}", e);
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
        LOG.info("Sms with id {} deleted", id);
    }

    /**
     * Start method uses to start communicate with USB modem
     */
    public void start() {
        try {
            serialPort = new SerialPort(SMS_PORT);
            serialPort.openPort ();
            serialPort.setParams (SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.writeBytes("AT\r".getBytes());
        } catch (SerialPortException e) {
            LOG.error("Unexpected error: {}", e);
        }
    }

    /**
     * Stop method uses to stom communicate with USB modem
     */
    public void stop() {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            LOG.error("Unexpected error: {}", e);
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

    private void waitSerialResponse() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            LOG.error("Unexpected error: {}", e);
        }
    }
}