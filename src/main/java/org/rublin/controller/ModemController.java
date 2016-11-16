package org.rublin.controller;

import jssc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Send mobile notification (sms or calls) using modem (usb modem or mobile phone
 */
public class ModemController {

    private static Logger LOG = LoggerFactory.getLogger(ModemController.class);

    private static SerialPort serialPort;
    private static final String AT_CALL = "ATD";
    private static final String AT_ANSWER_CALL = "ATA\r";
    private static final String AT_END_CALL = "ATH\r";
    private static final String AT_DIGITAL_MODE = "AT+CMGF=0";
    private static final String AT_TEXT_MODE = "AT+CMGF=1\r";
    private static final String AT_SMS = "AT+CMGS=";
    private static final String AT_READ_ALL_SMS = "AT+CMGL=\"ALL\"\r";

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
            return serialPort.readString();
        } catch (SerialPortException e) {
            LOG.error(e.getMessage());
        }
        return "ERROR";
    }

    public String receiveSms () throws SerialPortException {
        serialPort.writeBytes(AT_TEXT_MODE.getBytes());
        serialPort.writeBytes(AT_READ_ALL_SMS.getBytes());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        }
        return serialPort.readString();
    }

    public void deleteSms(int id) throws SerialPortException {
        serialPort.writeBytes(String.format("AT+CMGD=%d\r", id).getBytes());
    }

    public void start() {
        try {
            serialPort = new SerialPort ("COM5");
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
}