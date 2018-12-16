package org.rublin.model.user;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Sheremet on 04.09.2016.
 */
@Data
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")})
public class User {

    @Id
    @SequenceGenerator(name = "common_seq", sequenceName = "common_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "common_seq")
    private Integer id;

    @Column(name = "fname", nullable = false)
    //@NotEmpty
    private String firstName;
    @Column(name = "lname", nullable = false)
    //@NotEmpty
    private String lastName;

    @Column(name = "email", nullable = false)
    //@NotEmpty
    @Email
    private String email;

    @Column(name = "password", nullable = false)
    @NotEmpty
    @Length(min = 8)
    //@JsonView(View.REST.class)
    private String password;

    @Column(name = "telegram_id")
    private Integer telegramId;

    @Column(name = "telegram_name")
    private String telegramName;

    @Column(name = "mobile")
    private String mobile;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    //@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Role> roles;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "registered", columnDefinition = "timestamp default now()")
    private Date registered = new Date();

    public User() {}

    public User(String firstName, String lastName, Set<Role> roles, String email, String password, String mobile, String telegramName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.telegramName = telegramName;
        this.mobile = mobile;
        this.roles = roles;
        this.enabled = true;
    }
    public User(int id, String firstName, String lastName, Set<Role> roles, String email, String password, String mobile, String telegramName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.telegramName = telegramName;
        this.mobile = mobile;
        this.roles = roles;
        this.enabled = true;
    }
    public User(int id, String firstName, String lastName, Set<Role> roles, String email, String password, String mobile, String telegramName, int telegramId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.telegramName = telegramName;
        this.telegramId = telegramId;
        this.mobile = mobile;
        this.roles = roles;
        this.enabled = true;
    }

    public User(Integer id, String firstName, String lastName, String email, String password, String mobile, String telegramName, Role role, Role... roles) {
        this(id, firstName, lastName, EnumSet.of(role, roles), email, password, mobile, telegramName);
    }

    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }

    public boolean isNew() {
        return (this.id == null);
    }

    @Override
    public String toString() {
        return "User (" +
                "id=" + id +
                ", name=" + firstName +
                ", surname=" + lastName +
                ", email=" + email +
                ", roles=" + roles +
                ", telegram id=" + telegramId +
                ", telegram name=" + telegramName +
                ", mobile=" + mobile +
                ", enabled=" + enabled +
                ')';
    }
}
