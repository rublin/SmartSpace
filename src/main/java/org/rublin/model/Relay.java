package org.rublin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Relay {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "common_seq")
    private Integer id;

    @NotNull
    private String name;

    private String description;

    @Column(name = "productivity")
    private int productivityPerCent;

    private LocalDateTime updated;

    @PreUpdate
    public void preUpdate() {
        updated = LocalDateTime.now();
    }
}
