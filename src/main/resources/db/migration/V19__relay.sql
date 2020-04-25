CREATE TABLE relay
(
    id           INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
    zone_id      INTEGER NOT NULL,
    name         VARCHAR NOT NULL,
    description  VARCHAR,
    productivity INTEGER,
    updated      TIMESTAMP,
    FOREIGN KEY (zone_id) REFERENCES zones (id) ON DELETE CASCADE
);

INSERT INTO relay (zone_id, name, description, productivity)
VALUES (10, 'Pump', 'Heating pump neer the tank', 0);