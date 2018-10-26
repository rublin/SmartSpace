package org.rublin.util.sms;

public class InformationElementFactory {
    public static InformationElement createInformationElement(int id, byte[] data) {
        byte iei = (byte) (id & 0xFF);
        switch (iei) {
            case ConcatInformationElement.CONCAT_8BIT_REF:
            case ConcatInformationElement.CONCAT_16BIT_REF:
                return new ConcatInformationElement(iei, data);
            case PortInformationElement.PORT_16BIT:
                return new PortInformationElement(iei, data);
            default:
                return new InformationElement(iei, data);
        }
    }
}
