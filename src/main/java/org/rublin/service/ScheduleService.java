package org.rublin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.controller.ModemController;
import org.rublin.controller.NotificationService;
import org.rublin.model.ConfigKey;
import org.rublin.model.Zone;
import org.rublin.service.delayed.DelayQueueService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    private final ZoneService zoneService;
    private final SystemConfigService configService;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final DelayQueueService queueService;

    @Scheduled(fixedDelay = 10000)
    public void queueTake() {
        queueService.take();
    }

    @Scheduled(fixedDelay = 10000)
    public void queueHeatingTake() {
        queueService.takeHeating();
    }

    @Scheduled(fixedDelay = 60000)
    public void readSms() {
        log.debug("Scheduler is running");
        List<String> sms = modemController.readSms();
        if (!sms.isEmpty()) {
            log.info("Read {} messages", sms.size());
            StringBuffer sb = new StringBuffer();
            sms.forEach(s -> sb
//                    .append("http://www.smspdu.com/?action=ppdu&pdu=")
                    .append(s).append("\r<br>"));
            notificationService.sendEmailNotification("Sms received", sb.toString());
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void zoneActivityMonitor() {
        log.debug("Zone activity scheduler start");
        zoneService.activity();
    }

    @Scheduled(cron = "${zone.night.cron}")
    public void nightZoneSecure() {
        log.info("Cron job to secure night zone started...");
        zoneService.getAll().stream()
                .filter(Zone::isNightSecurity)
                .filter(zone -> !zone.isActive() && !zone.isSecure())
                .forEach(zone -> zoneService.setSecure(zone, true));
    }

    @PostConstruct
    private void init() {
        taskScheduler.schedule(new TimeNotificationTask(), new CronTrigger(configService.get(ConfigKey.FIRST_CRON_TIME_NOTIFICATION)));
        taskScheduler.schedule(new TimeNotificationTask(), new CronTrigger(configService.get(ConfigKey.SECOND_CRON_TIME_NOTIFICATION)));
    }

    class TimeNotificationTask implements Runnable {

        @Override
        public void run() {
            boolean activeZonePresents = zoneService.getAll().stream()
                    .anyMatch(Zone::isActive);
            log.info("Scheduled time job started. Active zones {}", activeZonePresents);
            if (activeZonePresents) {
                notificationService.sayTime();
            }
        }
    }
}
