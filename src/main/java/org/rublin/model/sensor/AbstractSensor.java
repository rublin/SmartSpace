package org.rublin.model.sensor;

import lombok.Getter;
import lombok.Setter;
import org.rublin.model.Zone;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractSensor {
    @Id
    @SequenceGenerator(name = "common_seq", sequenceName = "common_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "common_seq")
    protected Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id", nullable = false)
    protected Zone zone;

    @Column(name = "name", nullable = false)
    protected String name;

    protected boolean active;

}
