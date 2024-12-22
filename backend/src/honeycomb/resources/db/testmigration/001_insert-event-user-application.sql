
INSERT INTO events (event_id, title, description, location, start_date, end_date, organizer_id)
VALUES ('45353065-3834-3030-2d65-3239633d3431', 'Test', 'Ablblalball', 'Test Park',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', '123e4567-e89b-12d3-a456-426614174000'),
        ('67353065-3834-3030-2d65-3239633kjld', 'Test Event 2', 'blablabal', 'Linking Park',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', '123e4567-e89b-12d3-a456-426614174000'),
       ('17353065-3834-3030-2d65-3239633d3431', 'Test Event 3', 'tschüssi', 'Mühlheim',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', 'b23e4567-e89b-12d3-a456-426614174001'),
       ('17353065-3834-3030-2d65-3239633d3432', 'Test Event 4', 'Cooles Event und vieles mehr', 'Berlin',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', 'b23e4567-e89b-12d3-a456-426614174001');
INSERT INTO users (user_id, email, name, password, role)
VALUES ('df333e19-6ed6-4c8b-b86e-ef78943a98a4', 'showman@example.com', 'Hung Luu','password123', 'EXHIBITOR'),
       ('af333e19-6ed6-4c8b-b86e-ef78943a98a2', 'showmantest@example.com', 'Khang','password123', 'EXHIBITOR'),
       ('af333e19-6ed6-4c8b-b86e-ef78943a98a3', 'showmantest3@example.com', 'Andre Radler','password123', 'EXHIBITOR'),
       ('bf333e19-6ed6-4c8b-b86e-ef78943a98a4', 'showmantest4@example.com', 'Frank Reich','password123', 'EXHIBITOR'),
       ('123e4567-e89b-12d3-a456-426614174000', 'organizer1@example.com', 'Frank Stein','password123', 'ORGANIZER'),
       ('b23e4567-e89b-12d3-a456-426614174001', 'organizer2@example.com', 'Ash Ketchum','password123', 'ORGANIZER'),
       ('b23e4567-e89b-12d3-a456-426614174002', 'organizer3@example.com', 'Robert J','password123', 'ORGANIZER');
INSERT INTO applications (application_id, user_id, event_id, contact_information, business_name, type_of_business, previous_participation, message, status)
VALUES (
           'b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a',
           'df333e19-6ed6-4c8b-b86e-ef78943a98a4',
           '45353065-3834-3030-2d65-3239633d3431',
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
           '45353065-3834-3030-2d65-3239633d3431',
           'dfadf@example.com',
           'Luu Consults',
           'Consulting',
           true,
           'Looking forward to participating!',
           'IN_PROGRESS'
       ),
       (
           'c3456789-01cd-ef23-4567-8901cdef2345',
           'af333e19-6ed6-4c8b-b86e-ef78943a98a3',
           '45353065-3834-3030-2d65-3239633d3431',
           'dfadf@example.com',
           'IT Technik',
           'Software IT',
           true,
           'Looking forward to participating!',
           'IN_PROGRESS'
       ),
       (
           'c3456789-01cd-ef23-4567-8901cdef2346',
           'af333e19-6ed6-4c8b-b86e-ef78943a98a2',
           '17353065-3834-3030-2d65-3239633d3431',
           'dfadf@example.com',
           'IT Technik',
           'Software IT',
           true,
           'Looking forward to participating!',
           'CONTRACT'
       );
