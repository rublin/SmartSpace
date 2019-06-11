package org.rublin.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.util.List;

@Getter
@Setter
@ToString
public class OnTelegramFileNotifyEvent extends ApplicationEvent {

    private List<File> files;

    public OnTelegramFileNotifyEvent(@NotEmpty List<File> files) {
        super(files);
        this.files = files;
    }
}
