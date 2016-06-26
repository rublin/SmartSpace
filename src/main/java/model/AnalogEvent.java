package model;

import java.time.LocalDateTime;

/**
 * Created by Sheremet on 22.06.2016.
 */
public class AnalogEvent extends AbstractEvent<Double> {

    private final Double state;

    public AnalogEvent(AbstractTrigger trigger, Double state) {
        super(trigger);
        this.state = state;
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
