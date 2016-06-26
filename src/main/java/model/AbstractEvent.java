package model;

import java.time.LocalDateTime;

/**
 * Created by Sheremet on 22.06.2016.
 */
public abstract class AbstractEvent<T> implements Event<T>{
    protected LocalDateTime time;
    protected Trigger trigger;

    public AbstractEvent(Trigger trigger) {
        time = LocalDateTime.now();
        this.trigger = trigger;
    }
    @Override
    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public Trigger getTrigger() {
        return trigger;
    }
}
