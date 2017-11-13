package org.rublin.controller;

import org.rublin.service.TriggerService;
import org.rublin.service.ZoneService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * ???
 *
 * @author Ruslan Sheremet
 * @see
 * @since 1.0
 */
@Component
@EnableScheduling
public class ScheduleController {

    private static final Logger LOG = getLogger(ScheduleController.class);

    @Autowired
    private  ModemController modemController;

    @Autowired
    private Notification notification;

    @Autowired
    private TriggerService triggerService;

    @Autowired
    private ZoneService zoneService;

    @Scheduled(fixedDelay = 60000)
    public void readSms() {
        LOG.debug("Scheduler is running");
        List<String> sms = modemController.readSms();
        if (!sms.isEmpty()) {
            LOG.info("Read {} messages", sms.size());
            StringBuffer sb = new StringBuffer();
            sms.forEach(s -> sb.append("http://www.smspdu.com/?action=ppdu&pdu=").append(s).append("\r<br>"));
            notification.sendEmailNotification("Sms received", sb.toString());
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void zoneActivityMonitor() {
        LOG.debug("Zone activity scheduler start");
        zoneService.activity();
    }
}
