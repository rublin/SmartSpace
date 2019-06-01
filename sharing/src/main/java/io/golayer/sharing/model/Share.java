package io.golayer.sharing.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Share {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Sheet sheet;

    private String selection;

    @ManyToMany
    @JoinTable(name = "share_users",
            joinColumns = @JoinColumn(name = "share_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;
}
