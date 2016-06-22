package model;

import java.time.LocalDateTime;

/**
 * Created by Sheremet on 22.06.2016.
 */
public class AnalogEvent implements Event<Double> {
    private final LocalDateTime time;
    private final Double state;

    public AnalogEvent(Double state) {
        this.time = LocalDateTime.now();
        this.state = state;
    }

    @Override
    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public Double getState() {
        return state;
    }
}
