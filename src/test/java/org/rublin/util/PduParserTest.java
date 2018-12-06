package org.rublin.util;

import org.junit.Test;
import org.rublin.util.sms.Pdu;
import org.rublin.util.sms.PduParser;

import static org.junit.Assert.assertEquals;

public class PduParserTest {

    private static final String LANTIN_PDU = "069110090000F111000A9210299232900000AA0CC8F71D14969741F977FD07";
    private static final String CYRILLIC_PDU = "07918350000004F4440ED0D637396C7EBBCB0008810162015111218C050003850201041A043E043400200434043E044104420443043F04430020043200200406043D044204350440043D04350442002D041F043E043C04560447043D0438043A003A002000330030003900380036003100370038002E0020041D04350020043F043E043204560434043E043C043B044F04390442043500200439043E0433043E002004410442043E";

    @Test
    public void parsePdu() {
        PduParser parser = new PduParser();
        Pdu pdu = parser.parsePdu(LANTIN_PDU);
        assertEquals("How are you?", pdu.getDecodedText());

        pdu = parser.parsePdu(CYRILLIC_PDU);
        assertEquals("Код доступу в Інтернет-Помічник: 30986178. Не повідомляйте його сто", pdu.getDecodedText());
    }
}