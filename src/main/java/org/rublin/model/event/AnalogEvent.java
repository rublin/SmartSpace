package org.rublin.model.event;

import org.rublin.model.Trigger;
import org.rublin.model.Type;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * Created by Sheremet on 22.06.2016.
 */

@NamedQueries({
        @NamedQuery(name = AnalogEvent.GET, query = "SELECT e FROM AnalogEvent e WHERE e.trigger.id=:trigger_id ORDER BY e.time DESC"),
        @NamedQuery(name = AnalogEvent.GET_ALL, query = "SELECT e FROM AnalogEvent e WHERE e.type='ANALOG'"),
        @NamedQuery(name = AnalogEvent.GET_BETWEEN, query = "SELECT e FROM AnalogEvent e WHERE e.type='ANALOG' AND e.time BETWEEN :from AND :to")
})
@Entity
@Table(name = "events", uniqueConstraints = {@UniqueConstraint(columnNames = {"trigger_id", "date_time"}, name = "events_unique_trigger_datetime_idx")})
public class AnalogEvent extends AbstractEvent<Double> {

    public static final String GET = "AnalogEvent.get";
    public static final String GET_ALL = "AnalogEvent.getAllSorted";
    public static final String GET_BETWEEN = "AnalogEvent.getBetween";

    @Column(name = "analog_state")
    private Double state;

    public AnalogEvent() {
        super.setType(Type.DIGITAL);
    }

    public AnalogEvent(Trigger trigger, Double state) {
        super(trigger);
        this.state = state;
        super.setType(Type.ANALOG);
        time = LocalDateTime.now();
    }

    public AnalogEvent(Trigger trigger, Double state, LocalDateTime time) {
        super(trigger);
        this.state = state;
        super.setType(Type.ANALOG);
        super.setTime(time);
    }

    public AnalogEvent(int id, Trigger trigger, Double state, LocalDateTime time) {
        super(trigger);
        this.state = state;
        super.setType(Type.ANALOG);
        super.setTime(time);
        super.setId(id);
    }

    @Override
    public Double getState() {
        return state;
    }


    @Override
    public String toString() {
        return String.format("%s {id: %s, state: %.2f, time: %s}", this.getClass().getSimpleName(), (super.id == null ? "null" : getId().toString()), getState(), getTime().toString());
    }
}
