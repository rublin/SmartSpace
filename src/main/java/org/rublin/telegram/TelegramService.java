package org.rublin.telegram;

import org.rublin.to.TelegramResponseDto;
import org.telegram.telegrambots.api.objects.Message;

import java.util.Set;

public interface TelegramService {
    TelegramResponseDto process(Message message);

    Set<Long> getChatIds();
}
