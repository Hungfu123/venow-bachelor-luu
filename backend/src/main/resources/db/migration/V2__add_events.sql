INSERT INTO events (event_id, title, description, location, start_date, end_date, booths, organizer_id)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Fischstaebchen-Band', 'A large outdoor music festival', 'Central Park',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50,'123e4567-e89b-12d3-a456-426614174000'),
       ('550e8400-e29b-41d4-a716-446655440001', 'Thiens Schmackofatz', 'A conference for tech enthusiasts',
        'Convention Center', '2024-07-15 09:00:00', '2024-07-15 17:00:00', 50,'123e4567-e89b-12d3-a456-426614174001');

INSERT INTO users (user_id, email,name, password, role)
VALUES ('df333e19-6ed6-4c8b-b86e-ef78943a98a4', 'showman@example.com', 'Max Exi', 'password123', 'EXHIBITOR'),
       ('af333e19-6ed6-4c8b-b86e-ef78943a98a2', 'showmantest@example.com', 'Misty Exo', 'password123', 'EXHIBITOR');
