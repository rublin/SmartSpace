package model;

import java.time.LocalDateTime;

/**
 * Created by Sheremet on 21.06.2016.
 */
public class DigitEvent implements Event<Boolean>{
    private final LocalDateTime time;
    private final boolean state;

    public DigitEvent(boolean state) {
        time = LocalDateTime.now();
        this.state = state;
    }

    @Override
    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public Boolean getState() {
        return state;
    }
}
