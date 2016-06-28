package model;

import model.event.AnalogEvent;
import model.event.DigitEvent;
import model.event.Event;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class Trigger {
    private Integer id;
    private String name;
    private Event event;
    private Type type;

    public Trigger() {
    }
    public Trigger(Integer id) {
        this.id = id;
    }
    public Trigger(String name) {
        this.name = name;
        this.type = Type.DIGITAL;
        setEvent(new DigitEvent(this, Boolean.FALSE));
    }
    public Trigger(String name, Type type) {
        this.name = name;
        this.type = type;
        Event event = (type == Type.ANALOG) ?
                new AnalogEvent(this, 0.0) :
                new DigitEvent(this, Boolean.FALSE);
        setEvent(event);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public boolean isNew() {
        return (this.id == null);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Trigger{" +
                "id= " + getId() +
                ", name= " + getName() +
                ", type= " + getType() +
                //", state= " + event == null ? "null" : event.getState() + " at " +
                '}';
    }
}
