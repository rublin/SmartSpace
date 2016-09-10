package org.rublin.controller;

import org.rublin.model.Trigger;
import org.rublin.model.Zone;
import org.rublin.model.ZoneStatus;
import org.rublin.model.event.Event;
import org.rublin.service.StateService;
import org.rublin.service.ZoneService;
import org.rublin.util.Notification;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ruslan Sheremet (rublin) on 07.09.2016.
 */
@Controller
public class ZoneController {

    private static final Logger LOG = getLogger(ZoneController.class);

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private StateService eventService;

    public void setSecure(Zone zone, boolean secure) {
        zone.setSecure(secure);
        if (secure) {
            zoneService.save(zone);
        } else {
            zone.setStatus(ZoneStatus.GREEN);
            zoneService.save(zone);
        }
        LOG.info("change Zone secure state to {}", zone.getSecure());
        Notification.sendMail(String.format("Zone %s ", zone.getName()),
                String.format("<h1>Zone: <span style=\"color: blue;\">%s</span></h1>\n" +
                                "<h2>Status: <span style=\"color: %s;\">%s</span></h2>\n" +
                                "<h2>Secure: <span style=\"color: %s;\">%s</span></h2>",
                        zone.getName(),
                        zone.getStatus(),
                        zone.getStatus(),
                        zone.getSecure() ? "GREEN" : "GREY",
                        zone.getSecure()));
    }

    public void setStatus(Zone zone, ZoneStatus status) {
        zone.setStatus(status);
        zoneService.save(zone);
        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        Notification.sendMailWithAttach(String.format("Zone %s activity", zone.getName()),
                String.format("<h2>Zone: <span style=\"color: blue;\">%s</span></h2>\n" +
                                "%s",
                        zone.getName(),
                        getTriggerInfo(zone)), "http://192.168.0.31/Streaming/channels/1/picture");
    }

    public String getZoneInfo(Zone zone) {
        return String.format(
                "id: <b>%d</b>, name: <b>%s</b>, status: <b>%s</b>, secure: <b>%s</b>",
                zone.getId(),
                zone.getName(),
                zone.getStatus().toString(),
                zone.getSecure() ? "YES" : "NO");
    }

    public String getTriggerInfo(Zone zone) {
        StringBuilder result = new StringBuilder("<h2>Triggers</h2>\n" +
                "<table >\n" +
                "    <thead>\n" +
                "    <tr>\n" +
                "        <th>Name</th>\n" +
                "        <th>State</th>\n" +
                "    </tr>\n" +
                "    </thead>\n");
        for (Trigger t : zone.getTriggers()) {
            List<Event> events = eventService.get(t);
            result.append(String.format("<tr style=\"color: %s\">" +
                            "        <td>%s</td><td>%s</td>" +
                            "</tr>",
                    Boolean.parseBoolean(events.get(0).getState().toString()) ? "green" : "red",
                    t.getName(),
                    Boolean.parseBoolean(events.get(0).getState().toString()) ? "close | without move" : "open | with move"));
        }
        LOG.info(result.toString());
        return result.toString();
    }
}
