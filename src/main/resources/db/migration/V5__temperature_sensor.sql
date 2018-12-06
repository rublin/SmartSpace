CREATE TABLE temperature_sensors
(
  id          INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  zone_id     INTEGER NOT NULL,
  name        VARCHAR NOT NULL,
  min         REAL,
  max         REAL,
  active      BOOLEAN NOT NULL ,
  channel_id  integer,
  api_key     varchar(16) ,
  FOREIGN KEY (zone_id) REFERENCES zones (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX temperature_sensors_unique_name_idx
  ON temperature_sensors (name);

CREATE TABLE temperatures (
  id            INTEGER PRIMARY KEY DEFAULT nextval('event_seq'),
  sensor_id     INTEGER   NOT NULL,
  time          TIMESTAMP NOT NULL,
  temperature   REAL,
  FOREIGN KEY (sensor_id) REFERENCES temperature_sensors (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX temperatures_unique_trigger_datetime_idx
  ON temperatures (time);

INSERT INTO temperature_sensors (zone_id, name, min, max, active, channel_id, api_key) VALUES
  (10, 'Boiler high', 2, 95 , TRUE , NULL , NULL ),
  (10, 'Boiler low', 2, 95 , TRUE , NULL , NULL );
