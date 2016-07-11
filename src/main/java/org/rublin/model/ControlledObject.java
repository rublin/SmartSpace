package org.rublin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheremet on 11.07.2016.
 */
public class ControlledObject {
    private int id;
    private String name;
    private ObjectStatus status;
    private ObjectSecure secure;

   private List<Trigger> triggers = new ArrayList<>();

    public ControlledObject() {
    }

    public ControlledObject(int id, String name, ObjectStatus status, ObjectSecure secure) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.secure = secure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectStatus getStatus() {
        return status;
    }

    public void setStatus(ObjectStatus status) {
        this.status = status;
    }

    public ObjectSecure getSecure() {
        return secure;
    }

    public void setSecure(ObjectSecure secure) {
        this.secure = secure;
    }
    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
    }
}
