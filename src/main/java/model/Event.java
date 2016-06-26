package model;

import java.time.LocalDateTime;

/**
 * Created by Sheremet on 22.06.2016.
 */
public interface Event<T>{
    LocalDateTime getTime();
    T getState();
    Trigger getTrigger();
}
