package model.event;

import model.Trigger;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Created by Sheremet on 22.06.2016.
 */
//@MappedSuperclass
//@Access(AccessType.FIELD)
public abstract class AbstractEvent<T> implements Event<T>{
    protected LocalDateTime time;
    protected Trigger trigger;
    protected Integer id;

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
    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public boolean isNew() {
        return this.id == null;
    }
}
