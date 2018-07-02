package org.rublin.model.event;

import org.rublin.model.Trigger;
import org.rublin.model.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Created by Sheremet on 22.06.2016.
 */
public interface Event<T>{
    LocalDateTime getTime();
    T getState();
    Trigger getTrigger();
    Integer getId();
    void setId(Integer id);
    boolean isNew();
    void setTime(LocalDateTime time);
    void setTrigger(Trigger trigger);
    Type getType();
    void setType(Type type);
    boolean isDigital();
    void setAlarm(boolean alarm);
    boolean isAlarm();

    String toString();
}
