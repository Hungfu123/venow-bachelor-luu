INSERT INTO events (event_id, title, description, location, start_date, end_date, booths, organizer_id)
VALUES
    ('45353065-3834-3030-2d65-3239633d3431', 'Test', 'Ablblalball', 'Test Park',
     '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50,'123e4567-e89b-12d3-a456-426614174000'),
    ('67353065-3834-3030-2d65-3239633d3432', 'Test Event 2', 'blablabal', 'Linking Park',
     '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50,'123e4567-e89b-12d3-a456-426614174000'),
    ('17353065-3834-3030-2d65-3239633d3431', 'Test Event 3', 'tschüssi', 'Mühlheim',
     '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50,'b23e4567-e89b-12d3-a456-426614174002'),
    ('17353065-3834-3030-2d65-3239633d3432', 'Test Event 4', 'Cooles Event und vieles mehr', 'Berlin',
     '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50,'b23e4567-e89b-12d3-a456-426614174001'),
    ('17353065-3834-3030-2d65-3239633d3433', 'Test Event 5', 'Cooles Event und vieles mehr', 'Berlin',
     '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50,'b23e4567-e89b-12d3-a456-426614174002');

INSERT INTO users (user_id, email, name, password, role)
VALUES
    ('df333e19-6ed6-4c8b-b86e-ef78943a98a4', 'showman@example.com', 'Hung Luu', 'password123', 'EXHIBITOR'),
    ('af333e19-6ed6-4c8b-b86e-ef78943a98a2', 'showmantest@example.com', 'Khang', 'password123', 'EXHIBITOR'),
    ('af333e19-6ed6-4c8b-b86e-ef78943a98a3', 'showmantest3@example.com', 'Andre Radler', 'password123', 'EXHIBITOR'),
    ('bf333e19-6ed6-4c8b-b86e-ef78943a98a4', 'showmantest4@example.com', 'Frank Reich', 'password123', 'EXHIBITOR'),
    ('123e4567-e89b-12d3-a456-426614174000', 'organizer1@example.com', 'Frank Stein', 'password123', 'ORGANIZER'),
    ('b23e4567-e89b-12d3-a456-426614174001', 'organizer2@example.com', 'Ash Ketchum', 'password123', 'ORGANIZER'),
    ('b23e4567-e89b-12d3-a456-426614174002', 'organizer3@example.com', 'Robert J', 'password123', 'ORGANIZER'),
    ('eb7a9506-25d5-422a-842e-938008c448d6', 'organizer4@example.com', 'Zig Arette', 'password123', 'ORGANIZER');

INSERT INTO applications (application_id, user_id, event_id, contact_information, business_name, type_of_business, previous_participation, message, status)
VALUES
    ('b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a', 'df333e19-6ed6-4c8b-b86e-ef78943a98a4',
     '45353065-3834-3030-2d65-3239633d3431', 'example@example.com', 'Sample Business', 'Food Truck', true,
     'Looking forward to participating!', 'IN_PROGRESS'),
    ('b79a8e9b-7d74-4a5f-93e1-73f451e1ae0b', 'af333e19-6ed6-4c8b-b86e-ef78943a98a2',
     '45353065-3834-3030-2d65-3239633d3431', 'dfadf@example.com', 'Luu Consults', 'Consulting', true,
     'Looking forward to participating!', 'IN_PROGRESS'),
    ('c3456789-01cd-ef23-4567-8901cdef2345', 'af333e19-6ed6-4c8b-b86e-ef78943a98a3',
     '45353065-3834-3030-2d65-3239633d3431', 'dfadf@example.com', 'IT Technik', 'Software IT', true,
     'Looking forward to participating!', 'IN_PROGRESS'),
    ('c3456789-01cd-ef23-4567-8901cdef2346', 'af333e19-6ed6-4c8b-b86e-ef78943a98a2',
     '17353065-3834-3030-2d65-3239633d3431', 'dfadf@example.com', 'IT Technik', 'Software IT', true,
     'Looking forward to participating!', 'CONTRACT'),
    ('c3456789-01cd-ef23-4567-8901cdef2348', 'eb7a9506-25d5-422a-842e-938008c448d6',
     '17353065-3834-3030-2d65-3239633d3433', 'dfadf@example.com', 'IT Technik', 'Software IT', true,
     'Looking forward to participating!', 'CONTRACT');

INSERT INTO contracts (contract_id, organizer_id, exhibitor_id, application_id, terms, price, booth_number, status)
VALUES
    ('9a330d97-f4fb-4596-ab33-b826a0919272', 'b23e4567-e89b-12d3-a456-426614174002', 'af333e19-6ed6-4c8b-b86e-ef78943a98a2','c3456789-01cd-ef23-4567-8901cdef2346', 'Basic Terms', 300, 'A10', 'CONTRACT'),
    ('34e433de-8d61-48e7-b74d-904ae6de0d2a', 'b23e4567-e89b-12d3-a456-426614174002', 'eb7a9506-25d5-422a-842e-938008c448d6','c3456789-01cd-ef23-4567-8901cdef2348','Basic Terms', 300, 'A10', 'CONTRACT'),
    ('551f8d69-996d-42c7-8b04-cd9e4ac6aa06', 'b23e4567-e89b-12d3-a456-426614174002', 'af333e19-6ed6-4c8b-b86e-ef78943a98a3','c3456789-01cd-ef23-4567-8901cdef2345','Basic Terms', 300, 'A10', 'CONFIRMED'),
    ('9452c6a7-6acc-4208-bbb3-f04396326fc2', 'b23e4567-e89b-12d3-a456-426614174002', 'af333e19-6ed6-4c8b-b86e-ef78943a98a2','c3456789-01cd-ef23-4567-8901cdef2345','Basic Terms', 300, 'A10', 'REJECTED');
