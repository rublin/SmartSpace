package org.rublin.model;

import javax.persistence.*;
import java.io.File;

/**
 * Created by Sheremet on 29.08.2016.
 */
@NamedQueries({
//        @NamedQuery(name = Camera.GET, query = "SELECT c FROM Camera c WHERE c.id=:id"),
        @NamedQuery(name = Camera.GET_ALL_SORTED, query = "SELECT c FROM Camera c ORDER BY c.name"),
        @NamedQuery(name = Camera.GET_BY_ZONE, query = "SELECT c FROM Camera c WHERE c.zone=:zone"),
        @NamedQuery(name = Camera.DELETE, query = "DELETE FROM Camera c WHERE c.id=:id")
})
@Entity
@Table(name = "cameras")
public class Camera {

//    public static final String GET = "Camera.get";
    public static final String GET_ALL_SORTED = "Camera.getAllSorted";
    public static final String GET_BY_ZONE = "Camera.getByZone";
    public static final String DELETE = "Camera.delete";
    @Id
    @SequenceGenerator(name = "common_seq", sequenceName = "common_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "common_seq")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "url", nullable = false)
    private String URL;

    @Column(name = "ip", nullable = false)
    private String ip;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    public Camera() {
    }

    public Camera(String name, String ip, String login, String password, String URL, Zone zone) {
        this.name = name;
        this.ip = ip;
        this.login = login;
        this.password = password;
        this.URL = URL;
        this.zone = zone;
    }
    public Camera(int id, String name, String ip, String login, String password, String URL, Zone zone) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.login = login;
        this.password = password;
        this.URL = URL;
        this.zone = zone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", URL='" + URL + '\'' +
                ", ip='" + ip + '\'' +
                ", zone=" + zone +
                '}';
    }
}
