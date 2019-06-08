package org.rublin.events;

import lombok.Getter;
import lombok.Setter;
import org.rublin.model.user.User;
import org.springframework.context.ApplicationEvent;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class OnTelegramNotifyEvent extends ApplicationEvent {

    private String message;
    private List<File> files;
    private User user;

    public OnTelegramNotifyEvent(String message, List<File> files, User user) {
        super(message);
        this.message = message;
        this.files = files;
        this.user = user;
    }
}
