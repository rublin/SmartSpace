package model;

import java.time.LocalDateTime;

/**
 * Created by Sheremet on 21.06.2016.
 */
public class DigitEvent implements Event<Boolean>{
    private final LocalDateTime time;
    private final boolean state;
    private AbstractTrigger trigger;
    public DigitEvent(AbstractTrigger trigger, boolean state) {
        time = LocalDateTime.now();
        this.state = state;
        this.trigger = trigger;
    }

    @Override
    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public Boolean getState() {
        return state;
    }

    @Override
    public AbstractTrigger getTrigger() {
        return trigger;
    }

    @Override
    public String toString() {
        return "DigitalEvent {" +
                "trigger: " + getTrigger().getName() +
                ", state: " + getState() +
                ", time: " + getTime() + "}";
    }
}
