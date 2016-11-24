package org.rublin.main;

import org.rublin.controller.ModemController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class Main {
    private static final int ZONE_ID = 10;
    public static void main(String[] args) throws InterruptedException {
        /*
        InMemoryObjectRepository objectRepository = new InMemoryObjectRepository();
        InMemoryTriggerRepository triggerRepository = new InMemoryTriggerRepository();
        InMemoryEventRepository stateRepository = new InMemoryEventRepository();
        Zone obj = objectRepository.get(ZONE_ID);
        //stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);
        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),false));
        stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);
        triggerRepository.save(new Trigger("org.rublin.main Move 1 floor"), obj);
        triggerRepository.save(new Trigger("org.rublin.main Door 1 floor"), obj);
        triggerRepository.getAll().forEach(System.out::println);

        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),true));
        //hread.sleep(1000);
        stateRepository.save(triggerRepository.get(2), new DigitEvent(triggerRepository.get(2),false));
        Thread.sleep(1000);
        stateRepository.save(triggerRepository.get(2), new DigitEvent(triggerRepository.get(2),true));
        //Thread.sleep(5000);
        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),false));
        stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);

        Trigger analog = triggerRepository.save(new Trigger("FAT"), obj);
        stateRepository.save(triggerRepository.get(analog.getId()), new AnalogEvent(analog, 12.0));
        stateRepository.get(triggerRepository.get(analog.getId())).forEach(System.out::println);
        */
//        ModemController modem = new ModemController();
//        modem.start();
//        modem.sendSms("+380950724287", "Zone Zone1 notification:\n" +
//                "Trigger: <b>Trigger1</b>; Status: <b>BAD</b>".replaceAll("\\<.*\\>", ""));
//        modem.stop();
//        System.out.println("Zone Zone1 notification:\nTrigger: <b>Trigger1</b>; Status: <b>BAD</b>".replaceAll("<[^>]*>", ""));


        ModemController modemController = new ModemController();
        modemController.start();
        List<String> sms = modemController.readSms();
        StringBuffer sb = new StringBuffer();
        sms.forEach(System.out::println);
        sms.forEach(s -> sb.append("http://www.smspdu.com/?action=ppdu&pdu=").append(s).append("\r"));
        System.out.println(sb);
//        sms.forEach("www.smspdu.com/?action=ppdu&pdu="::concat);
//        System.out.println(String.join("\r", sms));
//        emailController.sendMail("SMS received", String.join("\r", sms), Arrays.asList("toor.ua@gmail.com"));
//        modemController.readSms().forEach(System.out::println);
//        System.out.println(modemController.readSms(0));
//        System.out.println(modemController.readSms(3));
    }
}
