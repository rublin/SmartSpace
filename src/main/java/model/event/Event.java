package model.event;

import model.Trigger;
import model.Type;

import java.time.LocalDateTime;

/**
 * Created by Sheremet on 22.06.2016.
 */
public interface Event<T>{
    LocalDateTime getTime();
    T getState();
    Trigger getTrigger();
    Integer getId();
    void setId(int id);
    boolean isNew();
    void setTime(LocalDateTime time);
    void setTrigger(Trigger trigger);
    Type getType();
    void setType(Type type);
}
