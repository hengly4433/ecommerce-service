-- RBAC Tables
CREATE TABLE permissions (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(100) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

CREATE TABLE roles (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(100) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

CREATE TABLE role_permissions (
  role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
  permission_id BIGINT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
  PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(320) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  locked BOOLEAN NOT NULL DEFAULT FALSE,
  email_verified BOOLEAN NOT NULL DEFAULT TRUE,
  profile_image_url TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

CREATE TABLE user_roles (
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
  PRIMARY KEY (user_id, role_id)
);

-- Core Master Data Tables
CREATE TABLE stores (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(100) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  address_line TEXT,
  city VARCHAR(120),
  country VARCHAR(120),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

CREATE TABLE categories (
  id BIGSERIAL PRIMARY KEY,
  parent_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
  name VARCHAR(255) NOT NULL,
  slug VARCHAR(255) NOT NULL UNIQUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

CREATE TABLE products (
  id BIGSERIAL PRIMARY KEY,
  sku VARCHAR(120) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price NUMERIC(12,2) NOT NULL,
  main_image_url TEXT,
  image_urls JSONB NOT NULL DEFAULT '[]'::JSONB,
  category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
  store_id BIGINT REFERENCES stores(id) ON DELETE SET NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

CREATE TABLE inventory (
  id BIGSERIAL PRIMARY KEY,
  product_id BIGINT NOT NULL UNIQUE REFERENCES products(id) ON DELETE CASCADE,
  quantity BIGINT NOT NULL DEFAULT 0,
  minimum_threshold BIGINT NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

CREATE TABLE customers (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(320) NOT NULL UNIQUE,
  full_name VARCHAR(255) NOT NULL,
  phone VARCHAR(50),
  shipping_address TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

-- Purchases (Incoming Stock) Tables
CREATE TABLE purchases (
  id BIGSERIAL PRIMARY KEY,
  supplier_name VARCHAR(255) NOT NULL,
  reference_no VARCHAR(120) UNIQUE,
  total_amount NUMERIC(12,2) NOT NULL,
  note TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

CREATE TABLE purchase_items (
  id BIGSERIAL PRIMARY KEY,
  purchase_id BIGINT NOT NULL REFERENCES purchases(id) ON DELETE CASCADE,
  product_id BIGINT NOT NULL REFERENCES products(id),
  unit_cost NUMERIC(12,2) NOT NULL,
  quantity BIGINT NOT NULL,
  line_total NUMERIC(12,2) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

-- Orders (Customer Sales) Tables
CREATE TYPE order_status AS ENUM ('NEW','PAID','SHIPPED','COMPLETED','CANCELLED');

CREATE TABLE orders (
  id BIGSERIAL PRIMARY KEY,
  order_no VARCHAR(120) NOT NULL UNIQUE,
  customer_id BIGINT REFERENCES customers(id) ON DELETE SET NULL,
  store_id BIGINT REFERENCES stores(id) ON DELETE SET NULL,
  status order_status NOT NULL DEFAULT 'NEW',
  subtotal NUMERIC(12,2) NOT NULL,
  discount NUMERIC(12,2) NOT NULL DEFAULT 0,
  tax NUMERIC(12,2) NOT NULL DEFAULT 0,
  total NUMERIC(12,2) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

CREATE TABLE order_items (
  id BIGSERIAL PRIMARY KEY,
  order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
  product_id BIGINT NOT NULL REFERENCES products(id),
  unit_price NUMERIC(12,2) NOT NULL,
  quantity BIGINT NOT NULL,
  line_total NUMERIC(12,2) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(255),
  updated_by VARCHAR(255) -- Corrected Name
);

-- Seed Minimal RBAC Data
INSERT INTO permissions(code,name, created_by) VALUES
 ('USER_READ','Read users', 'system'),('USER_WRITE','Write users', 'system'),
 ('ROLE_READ','Read roles', 'system'),('ROLE_WRITE','Write roles', 'system'),
 ('PRODUCT_READ','Read products', 'system'),('PRODUCT_WRITE','Write products', 'system'),
 ('ORDER_READ','Read orders', 'system'),('ORDER_WRITE','Write orders', 'system');

INSERT INTO roles(code,name, created_by) VALUES
 ('ADMIN','Administrator', 'system'),
 ('STAFF','Staff', 'system');

INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p WHERE r.code='ADMIN';

INSERT INTO users(email,password_hash,full_name,enabled,locked,email_verified, created_by)
VALUES ('admin@local', '{bcrypt}$2a$10$h1CBhV/0X3zP2K2Vt7q7EuY6tVt8DgHPZrF7b0exfQf0cO5c7o1L2', 'System Admin', TRUE, FALSE, TRUE, 'system');

INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.email='admin@local' AND r.code='ADMIN';

-- Seed CUSTOMER Role
INSERT INTO roles(code, name, created_by)
SELECT 'CUSTOMER', 'Customer', 'system'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE code = 'CUSTOMER');

-- Grant PRODUCT_READ to CUSTOMER Role
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.code='PRODUCT_READ'
LEFT JOIN role_permissions rp ON rp.role_id=r.id AND rp.permission_id=p.id
WHERE r.code='CUSTOMER' AND rp.role_id IS NULL;