ALTER TABLE users ADD telegram_chat_id BIGINT DEFAULT NULL  NULL;
CREATE UNIQUE INDEX users_telegram_chat_id_uindex ON public.users (telegram_chat_id);