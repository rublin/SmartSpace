package model;

/**
 * Created by Sheremet on 26.06.2016.
 */
public interface Trigger {
    Event getEvent();
    void setEvent(Event event);
    String getName();
    void setName(String name);
    Integer getId();
    void setId(Integer id);
    boolean isNew();
}
