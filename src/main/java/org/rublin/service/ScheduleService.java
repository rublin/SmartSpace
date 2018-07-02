package org.rublin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.controller.ModemController;
import org.rublin.controller.NotificationService;
import org.rublin.model.Zone;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Scheduled service
 *
 * @author Ruslan Sheremet
 * @see
 * @since 1.0
 */

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleService {


    private final ModemController modemController;
    private final NotificationService notificationService;
    private final TriggerService triggerService;
    private final ZoneService zoneService;

    @Scheduled(fixedDelay = 60000)
    public void readSms() {
        log.debug("Scheduler is running");
        List<String> sms = modemController.readSms();
        if (!sms.isEmpty()) {
            log.info("Read {} messages", sms.size());
            StringBuffer sb = new StringBuffer();
            sms.forEach(s -> sb.append("http://www.smspdu.com/?action=ppdu&pdu=").append(s).append("\r<br>"));
            notificationService.sendEmailNotification("Sms received", sb.toString());
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void zoneActivityMonitor() {
        log.debug("Zone activity scheduler start");
        zoneService.activity();
    }

    @Scheduled(cron = "0 50 6 * * MON-FRI")
    public void morningTimeSixFifty() {
        timeNotification();
    }

    @Scheduled(cron = "0 55 6 * * MON-FRI")
//    @Scheduled(cron = "0 18 10 * * MON-FRI")
    public void morningTimeSixFiftyFive() {
        timeNotification();
    }

    private void timeNotification() {
        boolean activeZonePresents = zoneService.getAll().stream()
                .anyMatch(Zone::isActive);
        log.info("Scheduled time job started. Active zones {}", activeZonePresents);
        if (activeZonePresents) {
            notificationService.sayTime();
        }
    }
}
