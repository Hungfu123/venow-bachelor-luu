INSERT INTO events (event_id, title, description, location, start_date, end_date, booths, organizer_id)
VALUES ('45353065-3834-3030-2d65-3239633d3431', 'Test', 'Ablblalball', 'Test Park',
        '2024-06-01 12:00:00', '2024-06-01 23:00:00', 50,'123e4567-e89b-12d3-a456-426614174000'),
       ('35353065-3834-3030-2d65-3239622d3432', 'Thiens Schmackofatz', 'A conference for tech enthusiasts',
        'Convention Center', '2024-07-15 09:00:00', '2024-07-15 17:00:00', 50,'123e4567-e89b-12d3-a456-426614174001');
INSERT INTO users (user_id, email, name, password, role)
VALUES ('df333e19-6ed6-4c8b-b86e-ef78943a98a4', 'showman@example.com', '', '$2a$12$kaxTXNM3I70LiEccd1PE8.Xpd8tOAO0r4RJN2b.OpcgEvh2kKx.Xu', 'EXHIBITOR'),
       ('af333e19-6ed6-4c8b-b86e-ef78943a98a2', 'showmantest@example.com', '', '$2a$12$vY8MFRxPHcvKC24r2.ERMOij.D/kboMLAVXGnNtycZGCY661yNkj.', 'EXHIBITOR'),
       ('123e4567-e89b-12d3-a456-426614174000', 'organizer3@example.com', 'Robert J', '$2a$12$fYIVAyMwyRElO.4wmmZJVu5c9stbTq1V561g0ffmm0UBbHQP1oNiC', 'ORGANIZER'),
       ('123e4567-e89b-12d3-a456-426614174001', 'organizer4@example.com', 'Zig Arette', '$2a$12$Ys6sn1UuXRVLgFXW0eob4ed.KLEedDN/dAcHBIFqsd7.N0w2Dmemq', 'ORGANIZER');
