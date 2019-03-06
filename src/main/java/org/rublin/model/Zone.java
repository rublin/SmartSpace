package org.rublin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Sheremet on 11.07.2016.
 */
@NamedQueries({
        @NamedQuery(name = Zone.GET, query = "SELECT z FROM Zone z WHERE z.id=:id"),
        @NamedQuery(name = Zone.GET_BY_SNAME, query = "SELECT z FROM Zone z WHERE z.shortName=:shortName"),
        @NamedQuery(name = Zone.GET_All_SORTED, query = "SELECT z FROM Zone z ORDER BY z.name"),
        @NamedQuery(name = Zone.DELETE, query = "DELETE FROM Zone z WHERE z.id=:id")
})
@Data
@Entity
@Table(name = "zones")
public class Zone {

    public static final String GET = "Zone.get";
    public static final String GET_BY_SNAME = "Zone.getByShortName";
    public static final String GET_All_SORTED = "ControllerObject.getAllSorted";
    public static final String DELETE = "ControllerObject.delete";

    @Id
    @SequenceGenerator(name = "zone_seq", sequenceName = "zone_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "zone_seq")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ZoneStatus status;

    @Column(name = "secure", nullable = false)
    private boolean secure;

    @Column(name = "security_changed")
    private LocalDateTime securityChanged;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany( mappedBy = "zone", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Trigger> triggers;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany( mappedBy = "zone", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Camera> cameras;

    @Column(name = "active")
    private boolean active;

    public Zone() {
    }

    public Zone(int id, String name) {
        this.id = id;
        this.name = name;
        this.shortName = name.substring(0,3);
        status = ZoneStatus.GREEN;
        secure = false;
    }

    public Zone(int id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        status = ZoneStatus.GREEN;
        secure = false;
    }


    public Zone(int id, String name, String shortName, ZoneStatus status, boolean secure) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.status = status;
        this.secure = secure;
    }

    public Zone(String name) {
        this.name = name;
        this.shortName = name.substring(0,3);
        this.status = ZoneStatus.GREEN;
        this.secure = false;
    }

    public Zone(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
        this.status = ZoneStatus.GREEN;
        this.secure = false;
    }

    @JsonIgnore
    public boolean isNew() {
        return id==null;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", name=" + name +
                ", shortName=" + shortName +
                ", status=" + status +
                ", secure=" + secure +
                '}';
    }
}
