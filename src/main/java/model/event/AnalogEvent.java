package model.event;

import model.Trigger;
import model.Type;

import javax.persistence.*;


/**
 * Created by Sheremet on 22.06.2016.
 */

@NamedQueries({
        @NamedQuery(name = AnalogEvent.GET, query = "SELECT e FROM AnalogEvent e WHERE e.id=:id"),
        @NamedQuery(name = AnalogEvent.GET_ALL_SORTED, query = "SELECT e FROM AnalogEvent e WHERE e.trigger.id=:trigger_id")
})
@Entity
@Table(name = "events", uniqueConstraints = {@UniqueConstraint(columnNames = {"trigger_id", "date_time"}, name = "events_unique_trigger_datetime_idx")})
public class AnalogEvent extends AbstractEvent<Double> {

    public static final String GET = "AnalogEvent.get";
    public static final String GET_ALL_SORTED = "AnalogEvent.getAllSorted";

    @Column(name = "analog_state")
    private Double state;

    public AnalogEvent() {
        super.setType(Type.DIGITAL);
    }

    public AnalogEvent(Trigger trigger, Double state) {
        super(trigger);
        this.state = state;
        super.setType(Type.DIGITAL);
    }

    @Override
    public Double getState() {
        return state;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "trigger: " + getTrigger().getName() +
                ", state: " + getState() +
                ", time: " + getTime() + "}";
    }
}
