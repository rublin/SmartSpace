package org.rublin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
