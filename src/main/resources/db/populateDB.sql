DELETE FROM trigger_type;
DELETE FROM events;
DELETE FROM triggers;
ALTER SEQUENCE global_seq RESTART WITH 1000;

INSERT INTO triggers (name) VALUES
  ('Door 1 floor'),
  ('Temperature 2 floor');

INSERT INTO trigger_type (type, trigger_id) VALUES
  ('DIGITAL', 1000),
  ('ANALOG', 1001);

INSERT INTO events (date_time, analog_state, digital_state, trigger_id) VALUES
  ('2016-06-20 11:00:00', NULL , TRUE, 1000),
  ('2016-05-30 11:00:00', NULL, FALSE , 1000),
  ('2016-05-30 20:00:00', NULL, TRUE , 1000),
  ('2016-05-31 10:00:00', NULL, FALSE, 1000),
  ('2016-05-31 13:00:00', NULL, TRUE, 1000),
  ('2016-05-31 20:00:00', NULL, FALSE, 1000),
  ('2016-06-01 14:00:00', 22.5, NULL, 1001),
  ('2016-06-01 21:00:00', 19.8, NULL, 1001);
