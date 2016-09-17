package org.rublin.model.event;

import org.rublin.model.Trigger;
import org.rublin.model.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Sheremet on 21.06.2016.
 */
@NamedQueries({
        @NamedQuery(name = DigitEvent.GET, query = "SELECT e FROM DigitEvent e WHERE e.trigger.id=:trigger_id ORDER BY e.time DESC "),
        @NamedQuery(name = DigitEvent.GET_ALL, query = "SELECT e FROM DigitEvent e WHERE e.type='DIGITAL'"),
        @NamedQuery(name = DigitEvent.GET_BETWEEN, query = "SELECT e FROM DigitEvent e WHERE e.type='DIGITAL' AND e.time BETWEEN :from AND :to")
})
@Entity
@Table(name = "events")
public class DigitEvent extends AbstractEvent<Boolean> {

    public static final String GET = "DigitEvent.get";
    public static final String GET_ALL = "DigitEvent.getAllSorted";
    public static final String GET_BETWEEN = "DigitEvent.getBetween";

    @Column(name = "digital_state")
    private Boolean state;


    public DigitEvent(Trigger trigger, Boolean state) {
        super(trigger);
        this.state = state;
        super.setType(Type.DIGITAL);
        time = LocalDateTime.now();
    }

    public DigitEvent(Trigger trigger, boolean state, LocalDateTime time) {
        super(trigger);
        this.state = state;
        super.setType(Type.DIGITAL);
        super.setTime(time);
    }

    public DigitEvent(int id, Trigger trigger, boolean state, LocalDateTime time) {
        super(trigger);
        this.state = state;
        super.setType(Type.DIGITAL);
        super.setTime(time);
        super.setId(id);
    }
    public DigitEvent() {
        super.setType(Type.DIGITAL);

    }

    @Override
    public Boolean getState() {
        return state;
    }

    @Override
    public String toString() {
        return String.format("%s {id: %s, state: %b, time: %s}", this.getClass().getSimpleName(), (super.id == null ? "null" : getId().toString()), getState(), getTime().toString());
    }
}
