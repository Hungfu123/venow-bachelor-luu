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
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_id VARCHAR(36) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       name VARCHAR(255) NOT NULL ,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       CONSTRAINT chk_role CHECK (role IN ('EXHIBITOR', 'ORGANIZER'))
);
CREATE TABLE applications (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              application_id VARCHAR(36) NOT NULL,
                              business_name VARCHAR(255) NOT NULL,
                              contact_information VARCHAR(255) NOT NULL,
                              event_id VARCHAR(36) NOT NULL,
                              message VARCHAR(255) NOT NULL,
                              previous_participation BOOLEAN NOT NULL,
                              status VARCHAR(255) NOT NULL,
                              type_of_business VARCHAR(255) NOT NULL,
                              user_id VARCHAR(36) NOT NULL,
                              UNIQUE(user_id, event_id)
);


INSERT INTO events (event_id, title, description, location, start_date, end_date, organizer_id)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Fischstaebchen-Band', 'A large outdoor music festival', 'Central Park',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', '123e4567-e89b-12d3-a456-426614174000'),
       ('550e8400-e29b-41d4-a716-446655440001', 'Thiens Schmackofatz', 'A conference for tech enthusiasts',
        'Convention Center', '2024-07-15 09:00:00', '2024-07-15 17:00:00', '123e4567-e89b-12d3-a456-426614174001');

INSERT INTO users (user_id, email,name, password, role)
VALUES ('df333e19-6ed6-4c8b-b86e-ef78943a98a4', 'showman@example.com', 'Max Exi', 'password123', 'EXHIBITOR'),
        ('af333e19-6ed6-4c8b-b86e-ef78943a98a2', 'showmantest@example.com', 'Misty Exo', 'password123', 'EXHIBITOR');
