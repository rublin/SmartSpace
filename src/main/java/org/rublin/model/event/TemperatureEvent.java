package org.rublin.model.event;

import lombok.Builder;
import lombok.Data;
import org.rublin.model.sensor.TemperatureSensor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "temperatures")
public class TemperatureEvent {

    @Id
    @SequenceGenerator(name = "event_seq", sequenceName = "event_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    private Integer id;

    private LocalDateTime time;

    private BigDecimal temperature;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private TemperatureSensor sensor;


}
