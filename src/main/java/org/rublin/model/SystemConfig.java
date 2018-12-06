package org.rublin.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "system_config")
public class SystemConfig {

    @Id
    @Enumerated(EnumType.STRING)
    private ConfigKey parameter;

    private String value;
}
