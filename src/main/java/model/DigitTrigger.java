package model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class DigitTrigger extends AbstractTrigger{
    private boolean state;
    public DigitTrigger(String name) {
        super(name);
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DigitTrigger{" +
                "id=" + super.getId() +
                ", name=" + super.getName() +
                ", state=" + state +
                '}';
    }
}
