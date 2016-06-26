package model;

import java.time.LocalDateTime;

/**
 * Created by Sheremet on 22.06.2016.
 */
public abstract class AbstractEvent implements Event{
    private final LocalDateTime time;

    public AbstractEvent(LocalDateTime time) {
        this.time = time;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "trigger: " + getTrigger().getName() +
                ", state: " + getState() +
                ", time: " + getTime() + "}";
    }
}
