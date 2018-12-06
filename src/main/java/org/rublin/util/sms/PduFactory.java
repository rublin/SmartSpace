package org.rublin.util.sms;

public class PduFactory {
    public static Pdu createPdu(int firstOctet) {
        Pdu pdu = null;
        int messageType = getFirstOctetField(firstOctet, PduUtils.TP_MTI_MASK);
        switch (messageType) {
            case PduUtils.TP_MTI_SMS_DELIVER:
                pdu = new SmsDeliveryPdu();
                break;
//            case PduUtils.TP_MTI_SMS_STATUS_REPORT:
//                pdu = new SmsStatusReportPdu();
//                break;
            case PduUtils.TP_MTI_SMS_SUBMIT:
                pdu = new SmsSubmitPdu();
                break;
            default:
                throw new RuntimeException("Invalid TP-MTI value: " + messageType);
        }
        // once set, you can't change it
        // this method is only available in this package
        pdu.setFirstOctet(firstOctet);
        return pdu;
    }

    private static int getFirstOctetField(int firstOctet, int fieldName) {
        return firstOctet & ~fieldName;
    }
}
