package org.rublin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "zones")
@NoArgsConstructor
@ToString(of = {"name", "status", "secure", "active"})
@EqualsAndHashCode(of = "name")
public class Zone {

    @Id
    @SequenceGenerator(name = "zone_seq", sequenceName = "zone_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "zone_seq")
    private Integer id;

    @NotBlank
    private String name;

    private String shortName;

    @Enumerated(EnumType.STRING)
    private ZoneStatus status;

    private boolean secure;

    private LocalDateTime securityChanged;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany( mappedBy = "zone", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Trigger> triggers;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany( mappedBy = "zone", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Camera> cameras;

    private boolean active;

    private boolean nightSecurity;

    private boolean morningDetector;

    private LocalDateTime lastMorningNotification;

    public Zone(int id, String name, String shortName, ZoneStatus status, boolean secure) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.status = status;
        this.secure = secure;
    }

    public Zone(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
        this.status = ZoneStatus.GREEN;
        this.secure = false;
    }

    public LocalDateTime getLastMorningNotification() {
        return Objects.isNull(lastMorningNotification) ? LocalDateTime.now().minusDays(1) : lastMorningNotification;
    }

    @JsonIgnore
    public boolean isNew() {
        return id==null;
    }

    @JsonIgnore
    public String getInfo() {
        return String.format(
                "id: <b>%d</b>, name: <b>%s</b>, status: <b>%s</b>, secure: <b>%s</b>",
                this.getId(),
                this.getName(),
                this.getStatus().toString(),
                this.isSecure() ? "YES" : "NO");    }
}
