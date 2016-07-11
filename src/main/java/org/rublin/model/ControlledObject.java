package org.rublin.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheremet on 11.07.2016.
 */
@NamedQueries({
        @NamedQuery(name = ControlledObject.GET, query = "SELECT o FROM ControlledObject o WHERE o.id=:id"),
        @NamedQuery(name = ControlledObject.GET_All_SORTED, query = "SELECT o FROM ControlledObject o ORDER BY o.name"),
        @NamedQuery(name = ControlledObject.DELETE, query = "DELETE FROM ControlledObject o WHERE o.id=:id")
})
@Entity
@Table(name = "objects")
public class ControlledObject {

    public static final String GET = "ControlledObject.get";
    public static final String GET_All_SORTED = "ControllerObject.getAllSorted";
    public static final String DELETE = "ControllerObject.delete";

    @Id
    @SequenceGenerator(name = "obj_seq", sequenceName = "obj_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "obj_seq")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ObjectStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "secure", nullable = false)
    private ObjectSecure secure;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "object", cascade = CascadeType.REMOVE)
    private List<Trigger> triggers;

    public ControlledObject() {
    }

    public ControlledObject(int id, String name) {
        this.id = id;
        this.name = name;
        status = ObjectStatus.GREEN;
        secure = ObjectSecure.NOT_PROTECTED;
    }

    public ControlledObject(int id, String name, ObjectStatus status, ObjectSecure secure) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.secure = secure;
    }

    public ControlledObject(String name) {
        this.name = name;
        this.status = ObjectStatus.GREEN;
        this.secure = ObjectSecure.NOT_PROTECTED;
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

    public boolean isNew() {
        return id==null;
    }

    @Override
    public String toString() {
        return "ControlledObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", secure=" + secure +
                '}';
    }
}
