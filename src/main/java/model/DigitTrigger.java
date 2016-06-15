package model;

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
}
