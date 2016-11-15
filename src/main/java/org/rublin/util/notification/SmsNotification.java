package org.rublin.util.notification;

import jssc.*;


public class SmsNotification {
    private static SerialPort serialPort;
    public static final String AT_CALL = "ATD";
    public static final String AT_ANSWER_CALL = "ATA\r";
    public static final String AT_END_CALL = "ATH\r";
    public static final String AT_DIGITAL_MODE = "AT+CMGF=0";
    public static final String AT_TEXT_MODE = "AT+CMGF=1\r";
    public static final String AT_SMS = "AT+CMGS=";
    public static final String AT_READ_ALL_SMS = "AT+CMGL=\"ALL\"\r";

    public static String call(String number) throws SerialPortException {
        serialPort.writeBytes((AT_CALL + number + ";\r").getBytes());
        return serialPort.readString();
    }

    public static String answerCall() throws SerialPortException {
        serialPort.writeBytes(AT_ANSWER_CALL.getBytes());
        return serialPort.readString();
    }

    public static String sendSms (String number, String message) throws SerialPortException {
        serialPort.writeBytes(AT_TEXT_MODE.getBytes());
        serialPort.writeBytes(String.format("%s\"%s\"\r", AT_SMS, number).getBytes());
        serialPort.writeBytes(String.format("%s\032\r", message).getBytes());
        return serialPort.readString();
    }

    public static String receiveSms () throws SerialPortException {
        serialPort.writeBytes(AT_TEXT_MODE.getBytes());
        serialPort.writeBytes(AT_READ_ALL_SMS.getBytes());
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return serialPort.readString();
    }

    public static void deleteSms(int id) throws SerialPortException {
        serialPort.writeBytes(String.format("AT+CMGD=%d\r", id).getBytes());
    }
    public static void main (String[] args) {

        /*String[] portNames = SerialPortList.getPortNames();
        for(int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
        }*/

        serialPort = new SerialPort ("COM5");
        try {
            serialPort.openPort ();
            serialPort.setParams (SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//            serialPort.
//            serialPort.setEventsMask (SerialPort.MASK_RXCHAR);
//            serialPort.addEventListener (new EventListener ());
            serialPort.writeBytes("AT\r".getBytes());
//            call("+380950724287");
//            answerCall();
//            deleteSms(11);
            Thread.sleep(100);
            System.out.println(receiveSms());

            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println (ex);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*private static class EventListener implements SerialPortEventListener {
        public void serialEvent (SerialPortEvent event) {
            if (event.isRXCHAR () && event.getEventValue () > 0){
                try {
                    String[] data = serialPort.readString (event.getEventValue ()).split("\r");
                }
                catch (SerialPortException ex) {
                    System.out.println (ex);
                }
            }
        }
    }*/
}