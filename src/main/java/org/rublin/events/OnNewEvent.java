package org.rublin.events;

import lombok.Getter;
import lombok.Setter;
import org.rublin.model.event.Event;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnNewEvent extends ApplicationEvent {
    private Event event;

    public OnNewEvent(Event event) {
        super(event);
        this.event = event;
    }
}
