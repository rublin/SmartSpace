package org.rublin.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.rublin.model.user.User;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class OnTelegramTextNotifyEvent extends ApplicationEvent {

    private String message;
    private User user;

    public OnTelegramTextNotifyEvent(@NotBlank String message, User user) {
        super(message);
        this.message = message;
        this.user = user;
    }
}
