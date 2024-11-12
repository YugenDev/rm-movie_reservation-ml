CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(10) CHECK (role IN ('admin', 'user')) NOT NULL
);


CREATE INDEX IF NOT EXISTS idx_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_email ON users(email);


INSERT INTO users (username, password_hash, email, role)
VALUES 
    ('juan_admin', 'hashed_password_123', 'juan.admin@example.com', 'admin'),
    ('maria_user', 'hashed_password_456', 'maria.user@example.com', 'user'),
    ('carlos_user', 'hashed_password_789', 'carlos.user@example.com', 'user'),
    ('ana_admin', 'hashed_password_abc', 'ana.admin@example.com', 'admin')
ON CONFLICT (username) DO NOTHING;
