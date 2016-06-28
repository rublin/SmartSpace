package model.event;

import model.Trigger;

/**
 * Created by Sheremet on 21.06.2016.
 */
public class DigitEvent extends AbstractEvent<Boolean> {

    private final boolean state;

    public DigitEvent(Trigger trigger, boolean state) {
        super(trigger);
        this.state = state;
    }

    @Override
    public Boolean getState() {
        return state;
    }

    @Override
    public String toString() {
        return "DigitalEvent {" +
                "id: " + (super.id == null ? "null" : getId()) +
                ", trigger: " + getTrigger().getName() +
                ", state: " + getState() +
                ", time: " + getTime() + "}";
    }
}
