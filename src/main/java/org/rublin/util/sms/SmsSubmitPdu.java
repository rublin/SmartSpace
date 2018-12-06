package org.rublin.util.sms;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
public class SmsSubmitPdu extends Pdu {
    private int messageReference = 0x00;
    private int validityPeriod = -1;
    private Calendar validityTimestamp;

    public int getTpVpf() {
        return getFirstOctetField(PduUtils.TP_VPF_MASK);
    }
}
