package org.rublin.events;

import org.springframework.context.ApplicationEvent;

public class OnHeatingStopEvent extends ApplicationEvent {

    public OnHeatingStopEvent(Object source) {
        super(source);
    }
}
