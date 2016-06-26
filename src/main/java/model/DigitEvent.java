package model;

/**
 * Created by Sheremet on 21.06.2016.
 */
public class DigitEvent extends AbstractEvent<Boolean> {

    private final boolean state;

    public DigitEvent(AbstractTrigger trigger, boolean state) {
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
                "trigger: " + getTrigger().getName() +
                ", state: " + getState() +
                ", time: " + getTime() + "}";
    }
}
