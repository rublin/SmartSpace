DROP INDEX public.temperatures_unique_trigger_datetime_idx RESTRICT;
CREATE INDEX temperatures_unique__index ON public.temperatures (id, time);
ALTER TABLE temperature_sensors ADD field_id int NULL;

INSERT INTO temperatures (sensor_id, time, temperature)
VALUES (106, '2018-08-30 09:00:00', 20.1),
       (106, '2018-08-30 09:10:00', 18.1),
       (107, '2018-08-30 09:10:00', 15.8);
