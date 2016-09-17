package org.rublin.service;

import org.rublin.model.Zone;
import org.rublin.model.Trigger;
import org.rublin.model.event.Event;
import org.rublin.util.exception.ExceptionUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.rublin.repository.TriggerRepository;
import org.rublin.util.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 15.06.2016.
 */
@Service
public class TriggerServiceImpl implements TriggerService {

    public static final Logger LOG = getLogger(TriggerServiceImpl.class);

    @Autowired
    private TriggerRepository repository;

    @Autowired
    private EventService eventService;

    @Override
    public Trigger save(Trigger trigger, Zone obj) {
        return repository.save(trigger, obj);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public Trigger get(int id) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public Collection<Trigger> getAll() {
        return repository.getAll();
    }

    @Override
    public String getInfo(Zone zone) {
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
