-- Add email verification flag
ALTER TABLE users ADD COLUMN IF NOT EXISTS email_verified boolean NOT NULL DEFAULT true;

-- Seed CUSTOMER role (minimal) if not present
INSERT INTO roles(code, name)
SELECT 'CUSTOMER', 'Customer'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE code = 'CUSTOMER');

-- Optionally grant PRODUCT_READ to CUSTOMER (idempotent)
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.code='PRODUCT_READ'
LEFT JOIN role_permissions rp ON rp.role_id=r.id AND rp.permission_id=p.id
WHERE r.code='CUSTOMER' AND rp.role_id IS NULL;

-- Ensure existing admin has email_verified=true (defensive, already default true)
UPDATE users SET email_verified=true WHERE email='admin@local';
