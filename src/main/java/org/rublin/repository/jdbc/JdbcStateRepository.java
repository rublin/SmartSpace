package org.rublin.repository.jdbc;

import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import org.rublin.repository.StateRepository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheremet on 27.06.2016.
 */
@Repository
public class JdbcStateRepository implements StateRepository {
    private static final BeanPropertyRowMapper<Event> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Event.class);
    public static final BeanPropertyRowMapper<DigitEvent> DIGIT_ROW_MAPPER = BeanPropertyRowMapper.newInstance(DigitEvent.class);
    public static final BeanPropertyRowMapper<AnalogEvent> ANALOG_ROW_MAPPER = BeanPropertyRowMapper.newInstance(AnalogEvent.class);

    @Autowired
    private JdbcTemplate jdbc;
//    @Autowired
//    private NamedParameterJdbcTemplate namedJdbc;
    private SimpleJdbcInsert insertEvent;

    @Autowired
    public JdbcStateRepository(DataSource dataSource) {
        this.insertEvent = new SimpleJdbcInsert(dataSource)
                .withTableName("events")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public void save(Trigger trigger, Event event) {
        MapSqlParameterSource map;
        if (event instanceof DigitEvent) {
            map = new MapSqlParameterSource()
                    .addValue("id", event.getId())
                    .addValue("date_time", event.getTime())
                    .addValue("digital_state", event.getState())
                    .addValue("trigger_id", trigger.getId());
        } else {
            map = new MapSqlParameterSource()
                    .addValue("id", event.getId())
                    .addValue("date_time", event.getTime())
                    .addValue("analog_state", event.getState())
                    .addValue("trigger_id", trigger.getId());
        }
        if (event.isNew()) {
            Number key = insertEvent.executeAndReturnKey(map);
            event.setId(key.intValue());
        }
    }

    @Override
    public List<Event> get(Trigger trigger) {
        List<Event> result;
        if (trigger.getType().equals(Type.DIGITAL)) {
            List<DigitEvent> digitEvents = jdbc.query("SELECT * FROM events WHERE trigger_id=? ORDER BY date_time DESC", DIGIT_ROW_MAPPER, trigger.getId());
            result = new ArrayList<>(digitEvents);
        } else {
            List<AnalogEvent> analogEvents = jdbc.query("SELECT * FROM events WHERE trigger_id=? ORDER BY date_time DESC", ANALOG_ROW_MAPPER, trigger.getId());
            result = new ArrayList<>(analogEvents);
        }

        return result;
    }

    @Override
    public List<Event> getAll() {
        return jdbc.query("SELECT * FROM events", ROW_MAPPER);
    }
}
