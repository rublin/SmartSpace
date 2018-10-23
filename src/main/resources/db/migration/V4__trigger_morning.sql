-- uses to GoodMorning with weather notification
ALTER TABLE public.triggers ADD morning_detector boolean DEFAULT false  NOT NULL;
INSERT INTO triggers (zone_id, name, type, secure, state, min, max, active, morning_detector) VALUES
  (10, 'Move 1 floor 2', 'DIGITAL', TRUE , TRUE ,0 , 0 , TRUE , true );
