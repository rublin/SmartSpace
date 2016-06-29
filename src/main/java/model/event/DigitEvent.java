package model.event;

import model.Trigger;
import model.Type;

import javax.persistence.*;

/**
 * Created by Sheremet on 21.06.2016.
 */
@NamedQueries({
        @NamedQuery(name = DigitEvent.GET, query = "SELECT e FROM DigitEvent e WHERE e.id=:id"),
        @NamedQuery(name = DigitEvent.GET_ALL_SORTED, query = "SELECT e FROM DigitEvent e WHERE e.trigger.id=:trigger_id")
})
@Entity
@Table(name = "events")
public class DigitEvent extends AbstractEvent<Boolean> {

    public static final String GET = "DigitEvent.get";
    public static final String GET_ALL_SORTED = "DigitEvent.getAllSorted";

    @Column(name = "digital_state")
    private boolean state;


    public DigitEvent(Trigger trigger, boolean state) {
        super(trigger);
        this.state = state;
        super.setType(Type.DIGITAL);
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
        return "DigitalEvent {" +
                "id: " + (super.id == null ? "null" : getId()) +
                //", trigger: " + getTrigger().getName() +
                ", state: " + getState() +
                ", time: " + getTime() + "}";
    }
}
