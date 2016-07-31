DROP TABLE IF EXISTS zones CASCADE ;
DROP TABLE IF EXISTS trigger_type CASCADE ;
DROP TABLE IF EXISTS triggers CASCADE ;
DROP TABLE IF EXISTS events;
DROP SEQUENCE IF EXISTS event_seq;
DROP SEQUENCE IF EXISTS trigger_seq;
DROP SEQUENCE IF EXISTS zone_seq;

CREATE SEQUENCE zone_seq START 10;
CREATE SEQUENCE event_seq START 1000;
CREATE SEQUENCE trigger_seq START 100;


CREATE TABLE zones
(
  id      INTEGER PRIMARY KEY DEFAULT nextval('obj_seq'),
  name    VARCHAR NOT NULL ,
  status  VARCHAR NOT NULL ,
  secure  BOOLEAN NOT NULL
);

CREATE TABLE triggers
(
  id        INTEGER PRIMARY KEY DEFAULT nextval('trigger_seq'),
  object_id INTEGER NOT NULL ,
  name      VARCHAR NOT NULL,
  type      VARCHAR NOT NULL,
  FOREIGN KEY (object_id) REFERENCES zones (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX triggers_unique_name_idx ON triggers (name);

CREATE TABLE events (
  id            INTEGER PRIMARY KEY DEFAULT nextval('event_seq'),
  trigger_id    INTEGER NOT NULL,
  type          TEXT NOT NULL,
  date_time     TIMESTAMP NOT NULL,
  analog_state  REAL,
  digital_state BOOLEAN,
  FOREIGN KEY (trigger_id) REFERENCES triggers (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX events_unique_trigger_datetime_idx ON events(trigger_id, date_time)