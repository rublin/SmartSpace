package org.rublin.model.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.*;

/**
 * Created by Sheremet on 04.09.2016.
 */
@NamedQueries({
        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id=:id"),
        @NamedQuery(name = User.BY_EMAIL, query = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email=:email"),
        @NamedQuery(name = User.BY_TELEGRAMID, query = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.telegramId=:telegramId"),
        @NamedQuery(name = User.BY_TELEGRAMNAME, query = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.telegramName=:telegramName"),
        @NamedQuery(name = User.BY_MOBILE, query = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.mobile=:mobile"),
        @NamedQuery(name = User.ALL_SORTED, query = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles ORDER BY u.email"),
})
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")})
public class User {

    public static final String GRAPH_WITH_ROLES = "User.withRoles";
    public static final String GRAPH_WITH_ROLES_AND_MEALS = "User.withRolesAndMeals";

    public static final String DELETE = "User.delete";
    public static final String ALL_SORTED = "User.getAllSorted";
    public static final String BY_EMAIL = "User.getByEmail";
    public static final String BY_TELEGRAMID = "User.getByTelegramId";
    public static final String BY_TELEGRAMNAME = "User.getByTelegramName";
    public static final String BY_MOBILE = "User.getByMobile";

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
    @ElementCollection(fetch = FetchType.LAZY)
    //@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Role> roles;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "registered", columnDefinition = "timestamp default now()")
    private Date registered = new Date();

    public User() {}

    public boolean isNew() {
        return (this.id == null);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Integer telegramId) {
        this.telegramId = telegramId;
    }

    public String getTelegramName() {
        return telegramName;
    }

    public void setTelegramName(String telegramName) {
        this.telegramName = telegramName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? Collections.emptySet() : EnumSet.copyOf(roles);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "User (" +
                "id=" + id +
                ", name=" + firstName +
                ", surname=" + lastName +
                ", email=" + email +
                ", roles=" + roles +
                ')';
    }
}
