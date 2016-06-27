package repository.jdbc;

import model.Trigger;
import model.event.DigitEvent;
import model.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import repository.StateRepository;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Sheremet on 27.06.2016.
 */
@Repository
public class JdbcStateRepository implements StateRepository {
    public static final BeanPropertyRowMapper<DigitEvent> ROW_MAPPER = BeanPropertyRowMapper.newInstance(DigitEvent.class);

    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;
    private SimpleJdbcInsert insertDigitalEvent;

    @Autowired
    public JdbcStateRepository (DataSource dataSource) {
        this.insertDigitalEvent = new SimpleJdbcInsert(dataSource)
                .withTableName("events")
                .usingGeneratedKeyColumns("id");
    }
    @Override
    public void save(Trigger trigger, Event event) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", event.getId())
                .addValue("date_time", event.getTime())
                .addValue("digital_state", event.getState())
                .addValue("trigger_id", trigger.getId());
        if (event.isNew()) {
            Number key = insertDigitalEvent.executeAndReturnKey(map);
            event.setId(key.intValue());
        }
    }

    @Override
    public List<Event> get(Trigger trigger) {
        //List<Event> events = jdbc.query("SELECT * FROM events WHERE trigger_id=?", ROW_MAPPER, trigger.getId());
        return null;
    }

    @Override
    public List<Event> getAll() {
        return null;
    }
}
