package org.rublin.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Sheremet on 11.07.2016.
 */
@NamedQueries({
        @NamedQuery(name = Zone.GET, query = "SELECT o FROM Zone o WHERE o.id=:id"),
        @NamedQuery(name = Zone.GET_All_SORTED, query = "SELECT o FROM Zone o ORDER BY o.name"),
        @NamedQuery(name = Zone.DELETE, query = "DELETE FROM Zone o WHERE o.id=:id")
})
@Entity
@Table(name = "zones")
public class Zone {

    public static final String GET = "Zone.get";
    public static final String GET_All_SORTED = "ControllerObject.getAllSorted";
    public static final String DELETE = "ControllerObject.delete";

    @Id
    @SequenceGenerator(name = "zone_seq", sequenceName = "zone_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "zone_seq")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ZoneStatus status;

    //@Enumerated(EnumType.STRING)
    @Column(name = "secure", nullable = false)
    private boolean secure;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "zone", cascade = CascadeType.REMOVE)
    private List<Trigger> triggers;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "zone", cascade = CascadeType.REMOVE)
    private List<Camera> cameras;

    public Zone() {
    }

    public Zone(int id, String name) {
        this.id = id;
        this.name = name;
        status = ZoneStatus.GREEN;
        secure = false;
    }

    public Zone(int id, String name, ZoneStatus status, boolean secure) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.secure = secure;
    }

    public Zone(String name) {
        this.name = name;
        this.status = ZoneStatus.GREEN;
        this.secure = false;
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

    public ZoneStatus getStatus() {
        return status;
    }

    public void setStatus(ZoneStatus status) {
        this.status = status;
    }

    public boolean getSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
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

    public boolean isNew() {
        return id==null;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", secure=" + secure +
                '}';
    }
}
