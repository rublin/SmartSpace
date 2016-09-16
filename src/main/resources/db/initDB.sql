DROP TABLE IF EXISTS zones CASCADE ;
DROP TABLE IF EXISTS trigger_type CASCADE ;
DROP TABLE IF EXISTS triggers CASCADE ;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS cameras;
DROP TABLE IF EXISTS user_roles CASCADE ;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS event_seq;
DROP SEQUENCE IF EXISTS common_seq;
DROP SEQUENCE IF EXISTS trigger_seq;
DROP SEQUENCE IF EXISTS zone_seq;

CREATE SEQUENCE zone_seq START 10;
CREATE SEQUENCE common_seq START 100;
CREATE SEQUENCE event_seq START 1000;


CREATE TABLE zones
(
  id        INTEGER PRIMARY KEY DEFAULT nextval('zone_seq'),
  name      VARCHAR NOT NULL ,
  short_name VARCHAR(4) NOT NULL ,
  status    VARCHAR NOT NULL ,
  secure    BOOLEAN NOT NULL
);
CREATE UNIQUE INDEX zone_short_name_idx ON zones (short_name);

CREATE TABLE cameras
(
  id      INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  zone_id INTEGER NOT NULL ,
  name    VARCHAR NOT NULL ,
  ip      VARCHAR NOT NULL ,
  url     VARCHAR NOT NULL ,
  login   VARCHAR NOT NULL ,
  password VARCHAR NOT NULL ,
  FOREIGN KEY (zone_id) REFERENCES zones (id) ON DELETE CASCADE
);

CREATE TABLE users
(
  id              INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  fname           VARCHAR NOT NULL ,
  lname           VARCHAR NOT NULL ,
  email           VARCHAR NOT NULL ,
  password        VARCHAR NOT NULL ,
  telegram_id     INTEGER,
  telegram_name   VARCHAR,
  mobile          VARCHAR,
  registered      TIMESTAMP DEFAULT now(),
  enabled         BOOLEAN DEFAULT TRUE
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);
CREATE UNIQUE INDEX users_unique_telegram_name_idx ON users (telegram_name);

CREATE TABLE user_roles
(
  user_id         INTEGER NOT NULL ,
  role            VARCHAR,
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE triggers
(
  id        INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  zone_id INTEGER NOT NULL ,
  name      VARCHAR NOT NULL,
  type      VARCHAR NOT NULL,
  secure    BOOLEAN NOT NULL ,
  state     BOOLEAN NOT NULL ,
  min       REAL,
  max       REAL,
  FOREIGN KEY (zone_id) REFERENCES zones (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX triggers_unique_name_idx ON triggers (name);

CREATE TABLE events (
  id            INTEGER PRIMARY KEY DEFAULT nextval('event_seq'),
  trigger_id    INTEGER NOT NULL,
  type          TEXT NOT NULL,
  date_time     TIMESTAMP NOT NULL,
  analog_state  REAL,
  digital_state BOOLEAN,
  alarm         BOOLEAN NOT NULL ,
  FOREIGN KEY (trigger_id) REFERENCES triggers (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX events_unique_trigger_datetime_idx ON events(trigger_id, date_time)