package org.rublin.util.sms;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
public class SmsDeliveryPdu extends Pdu {
    private Calendar timestamp;
}
