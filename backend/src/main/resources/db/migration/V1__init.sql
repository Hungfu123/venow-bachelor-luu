CREATE TABLE events
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id     VARCHAR(36)     NOT NULL,
    title        VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    location     VARCHAR(255) NOT NULL,
    start_date   TIMESTAMP    NOT NULL,
    end_date     TIMESTAMP    NOT NULL,
    organizer_id CHAR(36)     NOT NULL
);
INSERT INTO events (event_id, title, description, location, start_date, end_date, organizer_id)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Fischstaebchen-Band', 'A large outdoor music festival', 'Central Park',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', '123e4567-e89b-12d3-a456-426614174000'),
       ('550e8400-e29b-41d4-a716-446655440001', 'Thiens Schmackofatz', 'A conference for tech enthusiasts',
        'Convention Center', '2024-07-15 09:00:00', '2024-07-15 17:00:00', '123e4567-e89b-12d3-a456-426614174001');
