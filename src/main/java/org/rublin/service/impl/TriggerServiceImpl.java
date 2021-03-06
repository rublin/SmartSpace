package org.rublin.service.impl;

import org.rublin.model.Trigger;
import org.rublin.model.Zone;
import org.rublin.repository.TriggerRepository;
import org.rublin.service.TriggerService;
import org.rublin.util.exception.ExceptionUtil;
import org.rublin.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Trigger save(Trigger trigger, Zone obj) {
        return repository.save(trigger, obj);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public void delete(List<Zone> zoneList) throws NotFoundException {
        repository.deleteAllByZone(zoneList);
    }

    @Override
    public Trigger get(int id) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public List<Trigger> getByState(boolean state) {
        return repository.getByState(state);
    }

    @Override
    public Collection<Trigger> getAll() {
        return repository.getAll();
    }

    @Override
    public String getHtmlInfo(Zone zone) {
        StringBuilder result = new StringBuilder("<h2>Triggers</h2>\n" +
                "<table >\n" +
                "    <thead>\n" +
                "    <tr>\n" +
                "        <th>Name</th>\n" +
                "        <th>State</th>\n" +
                "    </tr>\n" +
                "    </thead>\n");
        for (Trigger t : zone.getTriggers()) {
            result.append(String.format("<tr style=\"color: %s\">" +
                            "        <td>%s</td><td>%s</td>" +
                            "</tr>",
                    t.isState() ? "green" : "red",
                    t.getName(),
                    t.isState() ? "GOOD" : "BAD"));
        }
        LOG.info(result.toString());
        return result.toString();
    }

    @Override
    public String getInfo(Zone zone) {
        StringBuilder result = new StringBuilder(String.format("Zone %s notification:\n", zone.getName()));
        for (Trigger t : zone.getTriggers()) {
            result.append(String.format("Trigger: <b>%s</b>; Status: <b>%s</b>\n",
                    t.getName(),
                    t.isState() ? "GOOD" : "BAD"));
        }
        return result.toString();
    }
}
