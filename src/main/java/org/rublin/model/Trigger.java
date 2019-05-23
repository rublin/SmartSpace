package org.rublin.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;
import org.rublin.model.sensor.AbstractSensor;

import javax.persistence.*;

/**
 * Created by Sheremet on 15.06.2016.
 */
@NamedQueries({
        @NamedQuery(name = Trigger.GET, query = "SELECT t FROM Trigger t WHERE t.id=:id"),
        @NamedQuery(name = Trigger.GET_ALL_SORTED, query = "SELECT t FROM Trigger t ORDER BY t.name"),
        @NamedQuery(name = Trigger.DELETE, query = "DELETE FROM Trigger t WHERE t.id=:id"),
        @NamedQuery(name = Trigger.GET_BY_STATE, query = "SELECT t FROM Trigger t WHERE t.state=:state"),
        @NamedQuery(name = Trigger.DELETE_BY_ZONE, query = "delete from Trigger where zone in :zone")
})
@Data
@Entity
@Table(name = "triggers")
@NoArgsConstructor
public class Trigger extends AbstractSensor {

    public static final String GET = "Trigger.get";
    public static final String GET_ALL_SORTED = "Trigger.getAllSorted";
    public static final String GET_BY_STATE = "Trigger.getByState";
    public static final String DELETE = "Trigger.delete";
    public static final String DELETE_BY_ZONE = "Trigger.delete.all.by.zone";

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "secure", nullable = false)
    private boolean secure;

    @Column(name = "state", nullable = false)
    private boolean state;

    @Column(name = "min")
    private Double minThreshold;

    @Column(name = "max")
    private Double maxThreshold;

    @Column(name = "morning_detector")
    private boolean morningDetector;

    public Trigger(String name) {
        this.name = name;
        this.type = Type.DIGITAL;
        //setEvent(new DigitEvent(this, Boolean.FALSE));
        state = true;
    }
    public Trigger(String name, Type type) {
        this.name = name;
        this.type = type;
        Event event = (type == Type.ANALOG) ?
                new AnalogEvent(this, 0.0) :
                new DigitEvent(this, Boolean.FALSE);
        //setEvent(event);
        state = true;
    }

    public Trigger(int id, Zone zone, String name, Type type, boolean secure, Double minThreshold, Double maxThreshold) {
        this.id = id;
        this.zone = zone;
        this.name = name;
        this.type = type;
        this.secure = secure;
        this.state = true;
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
    }

    public Trigger(int id, Zone zone, String name, Type type, boolean secure) {
        this.id = id;
        this.zone = zone;
        this.name = name;
        this.type = type;
        this.secure = secure;
        this.state = true;
        maxThreshold = 0.0;
        minThreshold = 0.0;
    }

    public Trigger(String name, Zone zone, Type type, boolean secure) {
        this.zone = zone;
        this.name = name;
        this.type = type;
        this.secure = secure;
        this.state = true;
        maxThreshold = 0.0;
        minThreshold = 0.0;
    }

    public Trigger(String name, Zone zone, Type type, boolean secure, Double minThreshold, Double maxThreshold) {
        this.zone = zone;
        this.name = name;
        this.type = type;
        this.secure = secure;
        this.state = true;
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
    }

    public boolean isNew() {
        return (this.id == null);
    }
}
