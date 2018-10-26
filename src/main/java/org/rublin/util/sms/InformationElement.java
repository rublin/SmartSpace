package org.rublin.util.sms;

public class InformationElement {
    private byte identifier;

    private byte[] data;

    // iei
    // iel (implicit length of data)
    // ied (raw ie data)
    InformationElement(byte id, byte[] ieData) {
        initialize(id, ieData);
    }

    InformationElement() {
    }

    // for outgoing messages
    void initialize(byte id, byte[] ieData) {
        this.identifier = id;
        this.data = ieData;
    }

    public int getIdentifier() {
        return (this.identifier & 0xFF);
    }

    public int getLength() {
        return this.data.length;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getSimpleName() + "[");
        sb.append(PduUtils.byteToPdu(this.identifier));
        sb.append(", ");
        sb.append(PduUtils.byteToPdu(this.data.length));
        sb.append(", ");
        sb.append(PduUtils.bytesToPdu(this.data));
        sb.append("]");
        return sb.toString();
    }
}
