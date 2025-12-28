-- Assuming a plain text password 'P@ssw0rd123' is hashed using BCrypt.
-- The actual hash should be generated in your application, but these mocks
-- are suitable for testing your password storage/comparison logic.
-- Mock Hash for P@ssw0rd123: $2a$10$012345678901234567890123456789012345678901234567890123

-- 1. SUPER_ADMIN (REQUIRED - automatically created, APPROVED by default)
INSERT INTO T_ACCOUNT (id, name, phone_number, nrc_number, password, status, role, created_at, updated_at)
VALUES ('b1e2c3d4-1111-4f00-9abc-123456789001',
        'Super Admin Max',
        '959000000000',
        '10/AdMin(SA)000000',
        '$2a$10$lEyt7GpnING/ezXrPkqT8ePB8ZRJMZsWGejOlvNuuBl3wrwr9qHNi',
        'APPROVED',
        'SUPER_USER',
        '2025-01-01 10:00:00',
        '2025-01-01 10:00:00');
--
-- 2. APPROVED USER (Can log in and manage projects) [cite: 30, 31]
INSERT INTO T_ACCOUNT (id, name, phone_number, nrc_number, password, status, role, created_at, updated_at)
VALUES ('c2f3d4e5-2222-4f00-9abc-123456789002',
        'Aung Myint',
        '959123456789',
        '12/KaTaNa(N)123456',
        '$2a$10$lEyt7GpnING/ezXrPkqT8ePB8ZRJMZsWGejOlvNuuBl3wrwr9qHNi',
        'APPROVED',
        'USER',
        '2025-01-10 11:30:00',
        '2025-01-10 11:30:00');

-- 3. PENDING USER (Registered but needs Super Admin approval to log in) [cite: 30, 42]
INSERT INTO T_ACCOUNT (id, name, phone_number, nrc_number, password, status, role, created_at, updated_at)
VALUES ('d3e4f5a6-3333-4f00-9abc-123456789003',
        'Htet Htet',
        '09234567890',
        '12/MaGaDa(N)654321',
        '$2a$10$012345678901234567890123456789012345678901234567890123',
        'REGISTERED',
        'USER',
        '2025-01-15 14:00:00',
        '2025-01-15 14:00:00');

---- 4. BLOCKED USER (Cannot register again) [cite: 44]
INSERT INTO T_ACCOUNT (id, name, phone_number, nrc_number, password, status, role, created_at, updated_at)
VALUES ('e4f5a6b7-4444-4f00-9abc-123456789004',
        'Zaw Min',
        '09345678901',
        '12/DaGaZa(N)987654',
        '$2a$10$012345678901234567890123456789012345678901234567890123',
        'BLOCKED',
        'USER',
        '2025-01-20 09:15:00',
        '2025-01-20 09:15:00');

-- 5. REJECTED USER (Can register again) [cite: 43]
INSERT INTO T_ACCOUNT (id, name, phone_number, nrc_number, password, status, role, created_at, updated_at)
VALUES ('f5a6b7c8-5555-4f00-9abc-123456789005',
        'Ei Mon',
        '09456789012',
        '12/PaDaSa(N)112233',
        '$2a$10$012345678901234567890123456789012345678901234567890123',
        'REJECTED',
        'USER',
        '2025-01-25 16:45:00',
        '2025-01-25 16:45:00');

-- 6-10. More Users for filtering tests
INSERT INTO T_ACCOUNT (id, name, phone_number, nrc_number, password, status, role, created_at, updated_at)
VALUES ('a6b7c8d9-6666-4f00-9abc-123456789006', 'Ko Ko', '09567890123', '01/SaPaKa(N)445566',
        '$2a$10$012345678901234567890123456789012345678901234567890123', 'APPROVED', 'USER', '2025-02-01 08:00:00',
        '2025-02-01 08:00:00'),
       ('b7c8d9e0-7777-4f00-9abc-123456789007', 'Mya Mya', '09678901234', '02/YaKaNa(N)778899',
        '$2a$10$012345678901234567890123456789012345678901234567890123', 'REGISTERED', 'USER', '2025-02-05 12:00:00',
        '2025-02-05 12:00:00'),
       ('c8d9e0f1-8888-4f00-9abc-123456789008', 'Chit Myat', '09789012345', '03/WaThaSa(N)001122',
        '$2a$10$012345678901234567890123456789012345678901234567890123', 'APPROVED', 'USER', '2025-02-10 14:30:00',
        '2025-02-10 14:30:00'),
       ('d9e0f1a2-9999-4f00-9abc-123456789009', 'Thet Naing', '09890123456', '04/DaNaMa(N)334455',
        '$2a$10$012345678901234567890123456789012345678901234567890123', 'REGISTERED', 'USER', '2025-02-15 10:10:00',
        '2025-02-15 10:10:00'),
       ('e0f1a2b3-aaaa-4f00-9abc-123456789010', 'Khine Zin', '09901234567', '05/MaDaNa(N)667788',
        '$2a$10$012345678901234567890123456789012345678901234567890123', 'REJECTED', 'USER', '2025-02-20 17:00:00',
        '2025-02-20 17:00:00');

--approved=4
--registered=3
--blocked=1
--rejected=2

--Projects queries
INSERT INTO "T_PROJECT" (id,
                         title,
                         description,
                         is_deleted,
                         account_id,
                         created_at,
                         updated_at)
VALUES (
           -- Project 1: Initial Backend Setup
           'a0b1c2d3-1111-4f00-9abc-123456789010',
           'User Registration API',
           'Develop secure endpoints for user sign-up with phone/NRC/password validation.',
           FALSE,
           'c2f3d4e5-2222-4f00-9abc-123456789002',
           '2025-12-01 10:00:00',
           '2025-12-01 10:00:00'),
       (
           -- Project 2: Security Implementation
           'a0b1c2d3-2222-4f00-9abc-123456789010',
           'JWT Authentication Flow',
           'Implement token generation and validation for secure login/access.',
           FALSE,
           'c2f3d4e5-2222-4f00-9abc-123456789002',
           '2025-12-01 11:00:00',
           '2025-12-01 11:00:00'),
       (
           -- Project 3: User Approval Workflow
           'a0b1c2d3-3333-4f00-9abc-123456789010',
           'Admin User Status Management',
           'Create endpoints for Super Admin to approve, reject, block users.',
           FALSE,
           'e0f1a2b3-aaaa-4f00-9abc-123456789010',
           '2025-12-01 12:00:00',
           '2025-12-01 12:30:00' -- Updated after creation
       ),
       (
           -- Project 4: Core Project CRUD (Create)
           'a0b1c2d3-4444-4f00-9abc-123456789010',
           'Create Project Endpoint',
           'Enable approved users to create new projects.',
           FALSE,
           'e0f1a2b3-aaaa-4f00-9abc-123456789010',
           '2025-12-01 13:00:00',
           '2025-12-01 13:00:00'),
       (
           -- Project 5: Core Project CRUD (Update/Delete)
           'a0b1c2d3-5555-4f00-9abc-123456789010',
           'Update and Delete Project Endpoints',
           'Implement logic to ensure users only manage their own projects.',
           FALSE,
           'e0f1a2b3-aaaa-4f00-9abc-123456789010',
           '2025-12-01 14:00:00',
           '2025-12-01 15:15:00' -- Updated later to include Delete logic
       ),
       (
           -- Project 6: Blacklist Feature
           'a0b1c2d3-6666-4f00-9abc-123456789010',
           'Blacklist Phone/NRC Check',
           'Prevent registration if phone or NRC is on the blacklist[cite: 29].',
           FALSE,
           'e0f1a2b3-aaaa-4f00-9abc-123456789010',
           '2025-12-01 16:00:00',
           '2025-12-01 16:00:00'),
       (
           -- Project 7: User Filtering (Admin)
           'a0b1c2d3-7777-4f00-9abc-123456789010',
           'Criteria API for User List',
           'Implement dynamic filtering for Super Admin on the list of users[cite: 39].',
           FALSE,
           'e0f1a2b3-aaaa-4f00-9abc-123456789010',
           '2025-12-01 17:00:00',
           '2025-12-01 17:00:00'),
       (
           -- Project 8: Project Filtering (User)
           'a0b1c2d3-8888-4f00-9abc-123456789010',
           'Criteria API for User Projects',
           'Implement dynamic filtering for Registered Users on their own projects[cite: 38].',
           FALSE,
           'e0f1a2b3-aaaa-4f00-9abc-123456789010',
           '2025-12-01 18:00:00',
           '2025-12-01 18:00:00'),
       (
           -- Project 9: Role-Based Access Control (RBAC)
           'a0b1c2d3-9999-4f00-9abc-123456789010',
           'Security Access Rules',
           'Define permissions: Super Admin can manage users but not projects; Users manage own projects[cite: 20, 21].',
           FALSE,
           'e0f1a2b3-aaaa-4f00-9abc-123456789010',
           '2025-12-01 19:00:00',
           '2025-12-01 19:00:00'),
       (
           -- Project 10: Soft Delete Bonus Feature
           'a0b1c2d3-aaaa-4f00-9abc-123456789010',
           'Soft Delete for T_PROJECT',
           'Change DELETE operation to set is_deleted=TRUE instead of permanent removal[cite: 46].',
           FALSE,
           'e0f1a2b3-aaaa-4f00-9abc-123456789010',
           '2025-12-01 20:00:00',
           '2025-12-02 09:00:00' -- Updated significantly later
       );

INSERT INTO T_BLACKLIST (
    id,
    phone_number,
    nrc_number,
    admin_id,
    created_at,
    updated_at
)
VALUES
    (
        '3f8a9c2e-6b14-4d8c-a2a7-9e3c1f0b7a91',
        '09123456789',
        '12/ABC(N)123456',
        'b1e2c3d4-1111-4f00-9abc-123456789001',
        '2025-12-21 08:00:00',
        '2025-12-21 08:00:00'
    ),
    (
        'a4c2b7d9-1e5f-43a6-9c8b-0d7e6f5a4b21',
        '09876543210',
        '5/DEF(N)654321',
        'b1e2c3d4-1111-4f00-9abc-123456789001',
        '2025-12-21 08:00:00',
        '2025-12-21 08:00:00'
    ),
    (
        '9d1f4a8c-2e7b-4c3a-b6e9-5a0f8d2c1b47',
        '09711223344',
        '7/GHI(N)112233',
        'b1e2c3d4-1111-4f00-9abc-123456789001',
        '2025-12-21 08:00:00',
        '2025-12-21 08:00:00'
    ),
    (
        'c7b5a9e2-8f1d-4a6c-9b3e-2d0f4c1a7e58',
        '09555666777',
        '9/JKL(N)556677',
        'b1e2c3d4-1111-4f00-9abc-123456789001',
        '2025-12-21 08:00:00',
        '2025-12-21 08:00:00'
    ),
    (
        '1e6d8f9a-3b4c-47a2-b0d5-9c2f1a7e8b64',
        '09222333444',
        '1/MNO(N)223344',
        'b1e2c3d4-1111-4f00-9abc-123456789001',
        '2025-12-21 08:00:00',
        '2025-12-21 08:00:00'
    );


