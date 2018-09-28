-- DELETE FROM trigger_type;

DELETE FROM zones;
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM cameras;
DELETE FROM triggers;
DELETE FROM events;
DELETE FROM system_config;

ALTER SEQUENCE zone_seq RESTART WITH 10;
ALTER SEQUENCE common_seq RESTART WITH 100;
ALTER SEQUENCE event_seq RESTART WITH 1000;

-- config
INSERT INTO system_config (parameter, value) VALUES
  ('TEXT_VOLUME', '80'),
  ('MUSIC_VOLUME', '50'),
  ('RADIO', 'http://192.99.147.61:8000'),
  ('FIRST_CRON_TIME_NOTIFICATION', '0 50 6 * * MON-FRI'),
  ('SECOND_CRON_TIME_NOTIFICATION', '0 55 6 * * MON-FRI');

-- user
INSERT INTO users (fname, lname, email, password, telegram_name, mobile)
VALUES ('Bohdan', 'Khmelnytsky', 'user@gmail.com', '$2a$10$Vof73Oba.CLUoJ.jg/2byuNx0pP9ts0apc.htTkAPvzRsceDzXzpe', 'Helenko', '+380957654321');

-- admin
INSERT INTO users (fname, lname, email, password, telegram_id, telegram_name, mobile)
VALUES ('Ivan', 'Mazepa', 'admin@gmail.com', '$2a$10$Vof73Oba.CLUoJ.jg/2byuNx0pP9ts0apc.htTkAPvzRsceDzXzpe', 241931659, 'rublinua', '+380951234567');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100),
  ('ROLE_ADMIN', 101);


INSERT INTO zones (name, short_name, status, secure, active) VALUES
  ('Home', 'h1', 'GREEN', FALSE, FALSE );
INSERT INTO cameras (zone_id, name, ip, url, login, password) VALUES
  (10, 'Cam 1 floor 1', '192.168.0.31', 'http://192.168.0.31/Streaming/channels/1/picture', 'imagesaver', 'QAZxsw123');
INSERT INTO triggers (zone_id, name, type, secure, state, min, max, active) VALUES
  (10, 'Door 1 floor', 'DIGITAL', TRUE , TRUE ,0 , 0 , TRUE ),
  (10, 'Temperature 2 floor', 'ANALOG', FALSE , TRUE , 15, 25, TRUE );

INSERT INTO events (type, date_time, analog_state, digital_state, trigger_id, alarm) VALUES
  ('DIGITAL', '2016-06-20 11:00:00', NULL , TRUE, 103, TRUE ),
  ('DIGITAL', '2016-05-30 11:00:00', NULL, FALSE , 103, TRUE ),
  ('DIGITAL', '2016-05-30 20:00:00', NULL, TRUE , 103, FALSE ),
  ('DIGITAL', '2016-05-31 10:00:00', NULL, FALSE, 103, FALSE ),
  ('DIGITAL', '2016-05-31 13:00:00', NULL, TRUE, 103, FALSE ),
  ('DIGITAL', '2016-05-31 20:00:00', NULL, FALSE, 103, FALSE ),
  ('ANALOG', '2016-06-01 14:00:00', 22.5, NULL, 104, FALSE ),
  ('ANALOG', '2016-06-01 21:00:00', 19.8, NULL, 104, FALSE );
