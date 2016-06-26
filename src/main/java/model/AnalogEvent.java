package model;

import java.time.LocalDateTime;

/**
 * Created by Sheremet on 22.06.2016.
 */
public class AnalogEvent implements Event<Double> {
    private final LocalDateTime time;
    private final Double state;
    private AbstractTrigger trigger;

    public AnalogEvent(AbstractTrigger trigger, Double state) {
        this.time = LocalDateTime.now();
        this.state = state;
        this.trigger = trigger;
    }

    @Override
    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public Double getState() {
        return state;
    }

    @Override
    public AbstractTrigger getTrigger() {
        return trigger;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "trigger: " + getTrigger().getName() +
                ", state: " + getState() +
                ", time: " + getTime() + "}";
    }
}
