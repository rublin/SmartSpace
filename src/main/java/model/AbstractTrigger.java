package model;

import model.event.Event;

/**
 * Created by Sheremet on 15.06.2016.
 */
public abstract class AbstractTrigger implements Trigger {
    protected Integer id;
    protected String name;
    protected Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    protected AbstractTrigger() {
    }
    protected AbstractTrigger (Integer id) {
        this.id = id;
    }
    protected AbstractTrigger(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Trigger{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", state=" + event.getState() + " at " + event.getTime() +
                '}';
    }
}
