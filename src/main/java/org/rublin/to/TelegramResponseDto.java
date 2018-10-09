package org.rublin.to;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.File;
import java.util.List;

@Builder
@Getter
@Setter
public class TelegramResponseDto {
    private String id;
    private List<String> messages;
    private List<File> files;
    private ReplyKeyboardMarkup keyboard;
}
