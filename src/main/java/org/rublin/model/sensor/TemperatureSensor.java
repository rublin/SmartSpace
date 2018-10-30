package org.rublin.model.sensor;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "temperature_sensors")
public class TemperatureSensor extends AbstractSensor {

    @Column(name = "min")
    private Double minThreshold;

    @Column(name = "max")
    private Double maxThreshold;

    @Column(name = "channel_id")
    private Integer thingSpeakChannelId;

    @Column(name = "api_key")
    private String thingSpeakApiKey;

    @Column(name = "field_id")
    private Integer fieldId;
}
