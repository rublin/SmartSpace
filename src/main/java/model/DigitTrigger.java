package model;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class DigitTrigger extends AbstractTrigger{
    public DigitTrigger(String name) {
        super(name);
        super.event = new DigitEvent(true);
    }

    public DigitEvent getEvent() {
        return (DigitEvent) super.event;
    }

    public void setEvent(DigitEvent event) {
        super.event = event;
    }

}
