package org.rublin.model.event;

import org.rublin.model.Trigger;
import org.rublin.model.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Sheremet on 22.06.2016.
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractEvent<T> implements Event<T>{

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    protected Integer id;

    @Column(name = "date_time", nullable = false, unique = true)
    protected LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trigger_id", nullable = false)
    protected Trigger trigger;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    protected Type type;

    public AbstractEvent() {}

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
    public boolean isNew() {
        return this.id == null;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    /*@Override
    public String toString() {
        return "DigitalEvent {" +
                "id: " + (id == null ? "null" : getId()) +
                //", trigger: " + getTrigger().getName() +
                ", state: " + getState() +
                ", time: " + getTime() + "}";
    }*/
}
