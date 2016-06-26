package model;

import model.event.AnalogEvent;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class AnalogTrigger extends AbstractTrigger{

    public AnalogTrigger (String name) {
        super(name);
        super.event = new AnalogEvent(this, 0.0);
    }
    public AnalogEvent getState() {
        return (AnalogEvent) super.event;
    }

    public void setState(AnalogEvent event) {
        super.event = event;
    }
}
