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
    -- 'user1' user gets ROLE_USER
    (
        (SELECT id FROM users WHERE username = 'user1'),
        (SELECT id FROM roles WHERE code = 'ROLE_USER')
    )
ON CONFLICT (user_id, role_id) DO NOTHING;