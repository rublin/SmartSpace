DROP TABLE IF EXISTS trigger_type CASCADE ;
DROP TABLE IF EXISTS triggers CASCADE ;
DROP TABLE IF EXISTS events;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START 1000;


CREATE TABLE triggers
(
  id        INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name      VARCHAR NOT NULL,
  type      VARCHAR NOT NULL

);
CREATE UNIQUE INDEX triggers_unique_name_idx ON triggers (name);

CREATE TABLE events (
  id            INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  trigger_id    INTEGER NOT NULL,
  type          TEXT NOT NULL,
  date_time     TIMESTAMP NOT NULL,
  analog_state  REAL,
  digital_state BOOLEAN,
  FOREIGN KEY (trigger_id) REFERENCES triggers (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX events_unique_trigger_datetime_idx ON events(trigger_id, date_time)