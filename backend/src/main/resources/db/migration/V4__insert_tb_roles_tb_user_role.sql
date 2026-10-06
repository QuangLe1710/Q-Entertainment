-- adminQuang / adminQuang
-- userQuang / userQuang

INSERT INTO users (username, email, password_hash, full_name, avatar_url)
VALUES
    ('adminQuang', 'adminQuang@gmail.com', '$2a$10$AlsbxKqAgkUApVBqWVOHjuNkpbH4rwRI6ifhhHrpJfUOfBkPfB4bq', 'Le Minh Quang', 'quang'),
    ('userQuang', 'userQuang@gmail.com', '$2a$10$Bb4Ta5LJCvc7TFJ2w8I1meAsGYbiN5sMonhtbpfAza.qgOagrcHGC', 'Le Minh Quang', 'quang')
ON CONFLICT (username) DO NOTHING;

INSERT INTO roles (code, name, description)
VALUES
    ('ROLE_ADMIN', 'Administrator', 'Full system access and management privileges'),
    ('ROLE_USER',  'Standard User', 'Regular access for standard operations')
ON CONFLICT (code) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
VALUES
    -- 'adminQuang' user gets ROLE_ADMIN
    (
        (SELECT id FROM users WHERE username = 'adminQuang'),
        (SELECT id FROM roles WHERE code = 'ROLE_ADMIN')
    ),
    -- 'userQuang' user gets ROLE_USER
    (
        (SELECT id FROM users WHERE username = 'userQuang'),
        (SELECT id FROM roles WHERE code = 'ROLE_USER')
    )
ON CONFLICT (user_id, role_id) DO NOTHING;