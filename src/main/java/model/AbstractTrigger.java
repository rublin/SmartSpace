package model;

/**
 * Created by Sheremet on 15.06.2016.
 */
public abstract class AbstractTrigger {
    protected Integer id;

    public AbstractTrigger() {
    }

    public AbstractTrigger(String name) {
        this.name = name;
    }

    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected AbstractTrigger (Integer id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public boolean isNew() {
        return (this.id == null);
    }

    @Override
    public String toString() {
        return "Trigger{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
