INSERT INTO events (event_id, title, description, location, start_date, end_date, booths, organizer_id)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Fischstaebchen-Band', 'A large outdoor music festival', 'Central Park',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50,'123e4567-e89b-12d3-a456-426614174000'),
       ('550e8400-e29b-41d4-a716-446655440001', 'Thiens Schmackofatz', 'A conference for tech enthusiasts',
        'Convention Center', '2024-07-15 09:00:00', '2024-07-15 17:00:00', 50,'123e4567-e89b-12d3-a456-426614174001'),
       ('ebb18719-8a28-4ce1-83cf-334ed4e25a70', 'Thiens Schmackofatz', 'A conference for tech enthusiasts für Hungi',
        'Convention Center', '2024-07-15 09:00:00', '2024-07-15 17:00:00', 50,'06f6633d-1b90-4a17-95fb-5f7157aa157e'),
       ('45353065-3834-3030-2d65-3239633d3431', 'Test', 'Ablblalball', 'Test Park',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50, '123e4567-e89b-12d3-a456-426614174000'),
       ('67353065-3834-3030-2d65-3239633kjld', 'Test Event 2', 'blablabal', 'Linking Park',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50, '123e4567-e89b-12d3-a456-426614174000'),
       ('17353065-3834-3030-2d65-3239633d3431', 'Test Event 3', 'tschüssi', 'Mühlheim',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50, '06f6633d-1b90-4a17-95fb-5f7157aa157e');

INSERT INTO users (user_id, email,name, password, role)
VALUES ('df333e19-6ed6-4c8b-b86e-ef78943a98a4', 'showman@example.com', 'Max Exi', 'password123', 'EXHIBITOR'),
       ('af333e19-6ed6-4c8b-b86e-ef78943a98a2', 'showmantest@example.com', 'Misty Exo', 'password123', 'EXHIBITOR'),
       ('06f6633d-1b90-4a17-95fb-5f7157aa157e', 'luu@example.com', 'Hung Luu', 'password123', 'ORGANIZER');

INSERT INTO applications (application_id, user_id, event_id, contact_information, business_name, type_of_business, previous_participation, message, status)
VALUES (
           'b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a',
           'df333e19-6ed6-4c8b-b86e-ef78943a98a4',
           'ebb18719-8a28-4ce1-83cf-334ed4e25a70',
           'example@example.com',
           'Sample Business',
           'Food Truck',
           true,
           'Looking forward to participating!',
           'IN_PROGRESS'
       ),
       (
           'b79a8e9b-7d74-4a5f-93e1-73f451e1ae0b',
           'af333e19-6ed6-4c8b-b86e-ef78943a98a2',
           'ebb18719-8a28-4ce1-83cf-334ed4e25a70',
           'dfadf@example.com',
           'Luu Consults',
           'Consulting',
           true,
           'Looking forward to participating!',
           'IN_PROGRESS'
       );

INSERT INTO contracts (contract_id, organizer_id, exhibitor_id, application_id, terms, price, booth_number, status)
VALUES
    ('9a330d97-f4fb-4596-ab33-b826a0919272', '06f6633d-1b90-4a17-95fb-5f7157aa157e', 'df333e19-6ed6-4c8b-b86e-ef78943a98a4','b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a', 'Basic Terms', 300, 'A10', 'CONTRACT'),
    ('34e433de-8d61-48e7-b74d-904ae6de0d2a', '06f6633d-1b90-4a17-95fb-5f7157aa157e', 'af333e19-6ed6-4c8b-b86e-ef78943a98a2','b79a8e9b-7d74-4a5f-93e1-73f451e1ae0b','Basic Terms', 300, 'A10', 'CONTRACT');