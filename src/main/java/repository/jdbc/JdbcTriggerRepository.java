package repository.jdbc;

import model.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import repository.TriggerRepository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

/**
 * Created by Sheremet on 27.06.2016.
 */
@Repository
public class JdbcTriggerRepository implements TriggerRepository {

    private static final BeanPropertyRowMapper<Trigger> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Trigger.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertTrigger;

    @Autowired
    public JdbcTriggerRepository (DataSource dataSource) {
        this.insertTrigger = new SimpleJdbcInsert(dataSource)
                .withTableName("triggers")
                .usingGeneratedKeyColumns("id");
    }
    @Override
    public Trigger save(Trigger trigger) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", trigger.getId())
                .addValue("name", trigger.getName());
        if (trigger.isNew()) {
            Number key = insertTrigger.executeAndReturnKey(map);
            trigger.setId(key.intValue());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE triggers SET name=:name WHERE id=:id", map);
        }
        return trigger;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM triggers WHERE id=?", id) !=0;
    }

    @Override
    public Trigger get(int id) {
        List<Trigger> triggers = jdbcTemplate.query("SELECT * FROM triggers WHERE id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(triggers);
    }

    @Override
    public Collection<Trigger> getAll() {
        return jdbcTemplate.query("SELECT * FROM triggers ORDER BY name", ROW_MAPPER);
    }
}
