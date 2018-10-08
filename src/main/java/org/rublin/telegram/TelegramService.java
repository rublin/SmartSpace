package org.rublin.telegram;

import org.rublin.to.TelegramResponseDto;
import org.telegram.telegrambots.api.objects.Message;

public interface TelegramService {
    TelegramResponseDto process(Message message);
}
