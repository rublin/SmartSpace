-- user
INSERT INTO flyway_schema_history (version_rank, installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success)
VALUES
  (2, 2, 2, 'populateDB', 'SQL', 'V2__populateDB.sql', 929674211, 'smart_u', '2018-10-19 16:03:49.775321', 92, TRUE),
  (3, 3, 3, 'zone mode', 'SQL', 'V3__zone_mode.sql', -486381508, 'smart_u', '2018-10-19 16:03:52.815994', 5, TRUE),
  (4, 4, 4, 'trigger morning', 'SQL', 'V4__trigger_morning.sql', 225165774, 'smart_u', '2018-10-22 16:38:03.352765', 568, TRUE),
  (5, 5, 5, 'temperature sensor', 'SQL', 'V5__temperature_sensor.sql', 1515360183, 'smart_u', '2018-10-30 10:19:11.595054', 460, TRUE),
  (6, 6, 6, 'populate temperature events', 'SQL', 'V6__populate_temperature_events.sql', -800708795, 'smart_u', '2018-10-30 12:02:29.764123', 460, TRUE),
  (7, 7, 10, 'grand', 'SQL', 'V10__grand.sql', 2065937610, 'smart_u', '2018-12-02 18:40:53.844925', 129, TRUE);
