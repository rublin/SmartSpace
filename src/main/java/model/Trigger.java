package model;

import model.event.AnalogEvent;
import model.event.DigitEvent;
import model.event.Event;

import javax.persistence.*;

/**
 * Created by Sheremet on 15.06.2016.
 */
@NamedQueries({
        @NamedQuery(name = Trigger.GET, query = "SELECT t FROM Trigger t WHERE t.id=:id"),
        @NamedQuery(name = Trigger.GET_ALL_SORTED, query = "SELECT t FROM Trigger t ORDER BY t.name"),
        @NamedQuery(name = Trigger.DELETE, query = "DELETE FROM Trigger t WHERE t.id=:id")
})
@Entity
@Table(name = "triggers")
public class Trigger {

    public static final String GET = "Trigger.get";
    public static final String GET_ALL_SORTED = "Trigger.getAllSorted";
    public static final String DELETE = "Trigger.delete";

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
   /*
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    public Trigger() {
    }
    public Trigger(Integer id) {
        this.id = id;
    }
    public Trigger(String name) {
        this.name = name;
        this.type = Type.DIGITAL;
        //setEvent(new DigitEvent(this, Boolean.FALSE));
    }
    public Trigger(String name, Type type) {
        this.name = name;
        this.type = type;
        Event event = (type == Type.ANALOG) ?
                new AnalogEvent(this, 0.0) :
                new DigitEvent(this, Boolean.FALSE);
        //setEvent(event);
    }

    public Trigger(int id, String name, Type type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
/*

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
*/

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
                //", type= " + getType() +
                //", state= " + event == null ? "null" : event.getState() + " at " +
                '}';
    }
}
