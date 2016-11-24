package org.rublin.controller;

import jssc.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private static final String AT_END_CALL = "ATH\r";
    private static final String AT_DIGITAL_MODE = "AT+CMGF=0";
    private static final String AT_TEXT_MODE = "AT+CMGF=1\r";
    private static final String AT_SMS = "AT+CMGS=";
    private static final String AT_READ_ALL_SMS = "AT+CMGL=\"ALL\"\r";
    private static final String AT_READ_SMS = "AT+CMGR=";

    /**
     * Make a call to phone number
     *
     * @param number phone number
     * @return response string or ERROR if exception happens
     */
    public String call(String number)  {
        try {
            serialPort.writeBytes((AT_CALL + number + ";\r").getBytes());
            return serialPort.readString();
        } catch (SerialPortException e) {
            LOG.error(e.getMessage());
        }
        return "ERROR";
    }

    public String answerCall() throws SerialPortException {
        serialPort.writeBytes(AT_ANSWER_CALL.getBytes());
        return serialPort.readString();
    }

    /**
     * Send sms with message to the number
     *
     * @param number mobile number
     * @param message sms text message
     * @return
     * @throws SerialPortException
     */
    public String sendSms (String number, String message)  {
        try {
            serialPort.writeBytes(AT_TEXT_MODE.getBytes());
            serialPort.writeBytes(String.format("%s\"%s\"\r", AT_SMS, number).getBytes());
            serialPort.writeBytes(String.format("%s\032\r", message).getBytes());
            LOG.info("Send sms to {} success", number);
            return serialPort.readString();
        } catch (SerialPortException e) {
            LOG.error(e.getMessage());
        }
        return "ERROR";
    }

    public List<String> readSms() {
        try {
            serialPort.writeBytes(AT_DIGITAL_MODE.getBytes());
            List<String> lines = new ArrayList<>();
            for (int i = 20; i >= 0; i--) {
                serialPort.writeBytes(String.format("%s%d%s", AT_READ_SMS, i, "\r").getBytes());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    LOG.error("Unexpected error: {}", e);
                }
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

    public void deleteSms(int id) throws SerialPortException {
        serialPort.writeBytes(String.format("AT+CMGD=%d\r", id).getBytes());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        }
        LOG.info("Sms with id {} deleted", id);
    }

    public void start() {
        try {
            serialPort = new SerialPort(SMS_PORT);
            serialPort.openPort ();
            serialPort.setParams (SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.writeBytes("AT\r".getBytes());
        } catch (SerialPortException e) {
            LOG.error(e.getMessage());
        }
    }

    public void stop() {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            LOG.error(e.getMessage());
        }
    }

    private String toPDU(String request) {
        List<String> lines = Arrays.asList(request.split("\r\n"));
        return lines.stream()
//                .findFirst()
                .filter(line -> line.startsWith("0"))
                .collect(Collectors.joining());
    }
}