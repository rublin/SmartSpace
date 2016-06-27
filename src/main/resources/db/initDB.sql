DROP TABLE IF EXISTS trigger_type;
DROP TABLE IF EXISTS triggers;
DROP TABLE IF EXISTS events;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START 1000;

CREATE TABLE triggers
(
  id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name       VARCHAR NOT NULL,
  registered TIMESTAMP DEFAULT now()
);
CREATE UNIQUE INDEX triggers_unique_name_idx ON triggers (name);

CREATE TABLE trigger_type
(
  trigger_id  INTEGER NOT NULL,
  type        VARCHAR,
  CONSTRAINT trigger_type_idx UNIQUE (trigger_id, type),
  FOREIGN KEY (trigger_id) REFERENCES triggers (id) ON DELETE CASCADE
);

CREATE TABLE events (
  id            INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  trigger_id    INTEGER NOT NULL,
  date_time     TIMESTAMP NOT NULL,
  analog_state  REAL,
  digital_state BOOLEAN,
  FOREIGN KEY (trigger_id) REFERENCES triggers (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX events_unique_trigger_datetime_idx ON events(trigger_id, date_time)