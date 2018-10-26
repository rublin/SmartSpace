package org.rublin.util.sms;

public class PortInformationElement extends InformationElement {
    public static final int PORT_16BIT = 0x05;

    PortInformationElement(byte id, byte[] data) {
        super(id, data);
        if (getIdentifier() != PORT_16BIT) {
            throw new RuntimeException("Invalid identifier " + getIdentifier() + " in data in: " + getClass().getSimpleName());
        }
        // iei
        // iel
        // dest(2 bytes)
        // src (2 bytes)
        if (data.length != 4) {
            throw new RuntimeException("Invalid data length in: " + getClass().getSimpleName());
        }
    }

    PortInformationElement(int identifier, int destPort, int srcPort) {
        super();
        byte[] data = null;
        switch (identifier) {
            case PORT_16BIT:
                data = new byte[4];
                data[0] = (byte) ((destPort & 0xFF00) >>> 8);
                data[1] = (byte) (destPort & 0xFF);
                data[2] = (byte) ((srcPort & 0xFF00) >>> 8);
                data[3] = (byte) (srcPort & 0xFF);
                break;
            default:
                throw new RuntimeException("Invalid identifier for " + getClass().getSimpleName());
        }
        initialize((byte) (identifier & 0xFF), data);
    }

    public int getDestPort() {
        // first 2 bytes of data
        byte[] data = getData();
        return (((data[0] & 0xFF) << 8) | (data[1] & 0xFF));
    }

    public int getSrcPort() {
        // next 2 bytes of data
        byte[] data = getData();
        return (((data[2] & 0xFF) << 8) | (data[3] & 0xFF));
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        sb.append("[Dst Port: ");
        sb.append(getDestPort());
        sb.append(", Src Port: ");
        sb.append(getSrcPort());
        sb.append("]");
        return sb.toString();
    }
}