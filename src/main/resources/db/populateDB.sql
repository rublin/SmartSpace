-- DELETE FROM trigger_type;

DELETE FROM objects;
DELETE FROM triggers;
DELETE FROM events;

ALTER SEQUENCE obj_seq RESTART WITH 10;
ALTER SEQUENCE trigger_seq RESTART WITH 100;
ALTER SEQUENCE event_seq RESTART WITH 1000;

INSERT INTO objects (name, status, secure) VALUES
  ('Home', 'GREEN', 'NOT_PROTECTED');

INSERT INTO triggers (object_id, name, type) VALUES
  (10, 'Door 1 floor', 'DIGITAL'),
  (10, 'Temperature 2 floor', 'ANALOG');

/*
  INSERT INTO trigger_type (type, trigger_id) VALUES
  ('DIGITAL', 1000),
  ('ANALOG', 1001);
  */

INSERT INTO events (type, date_time, analog_state, digital_state, trigger_id) VALUES
  ('DIGITAL', '2016-06-20 11:00:00', NULL , TRUE, 100),
  ('DIGITAL', '2016-05-30 11:00:00', NULL, FALSE , 100),
  ('DIGITAL', '2016-05-30 20:00:00', NULL, TRUE , 100),
  ('DIGITAL', '2016-05-31 10:00:00', NULL, FALSE, 100),
  ('DIGITAL', '2016-05-31 13:00:00', NULL, TRUE, 100),
  ('DIGITAL', '2016-05-31 20:00:00', NULL, FALSE, 100),
  ('ANALOG', '2016-06-01 14:00:00', 22.5, NULL, 101),
  ('ANALOG', '2016-06-01 21:00:00', 19.8, NULL, 101);
