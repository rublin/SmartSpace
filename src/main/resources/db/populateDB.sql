-- DELETE FROM trigger_type;

DELETE FROM zones;
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM cameras;
DELETE FROM triggers;
DELETE FROM events;

ALTER SEQUENCE zone_seq RESTART WITH 10;
ALTER SEQUENCE common_seq RESTART WITH 100;
ALTER SEQUENCE event_seq RESTART WITH 1000;

-- user
INSERT INTO users (fname, lname, email, password, telegram_name, mobile)
VALUES ('Olena', 'Sheremet', 'olenka@sheremet.org', 'P@ssw0rd', 'Helenko', '+380950724288');

-- admin
INSERT INTO users (fname, lname, email, password, telegram_id, telegram_name, mobile)
VALUES ('Ruslan', 'Sheremet', 'toor.ua@gmail.com', 'P@ssw0rd', 241931659, 'rublinua', '+380950724287');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100),
  ('ROLE_ADMIN', 101);


INSERT INTO zones (name, short_name, status, secure) VALUES
  ('Home', 'h1', 'GREEN', FALSE );
INSERT INTO cameras (zone_id, name, ip, url, login, password) VALUES
  (10, 'Cam 1 floor 1', '192.168.0.31', 'http://192.168.0.31/Streaming/channels/1/picture', 'imagesaver', 'QAZxsw123');
INSERT INTO triggers (zone_id, name, type, secure, state, min, max) VALUES
  (10, 'Door 1 floor', 'DIGITAL', TRUE , TRUE ,NULL , NULL ),
  (10, 'Temperature 2 floor', 'ANALOG', FALSE , TRUE , 15, 25);

INSERT INTO events (type, date_time, analog_state, digital_state, trigger_id, alarm) VALUES
  ('DIGITAL', '2016-06-20 11:00:00', NULL , TRUE, 103, TRUE ),
  ('DIGITAL', '2016-05-30 11:00:00', NULL, FALSE , 103, TRUE ),
  ('DIGITAL', '2016-05-30 20:00:00', NULL, TRUE , 103, FALSE ),
  ('DIGITAL', '2016-05-31 10:00:00', NULL, FALSE, 103, FALSE ),
  ('DIGITAL', '2016-05-31 13:00:00', NULL, TRUE, 103, FALSE ),
  ('DIGITAL', '2016-05-31 20:00:00', NULL, FALSE, 103, FALSE ),
  ('ANALOG', '2016-06-01 14:00:00', 22.5, NULL, 104, FALSE ),
  ('ANALOG', '2016-06-01 21:00:00', 19.8, NULL, 104, FALSE );
