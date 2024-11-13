CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE DATABASE IF NOT EXISTS reservations;


CREATE TABLE IF NOT EXISTS reservations (
    reservation_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    showtime_id UUID NOT NULL,
    reserved_seats JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_showtime FOREIGN KEY (showtime_id) REFERENCES showtimes (showtime_id) ON DELETE CASCADE
);

-- These users are not in sync with the users table in the user-db

INSERT INTO reservations (user_id, showtime_id, reserved_seats)
VALUES
    ('123e4567-e89b-12d3-a456-426614174000', '550e8400-e29b-41d4-a716-446655440003', '[{"seat_id": "A1"}, {"seat_id": "A3"}]'),
    ('123e4567-e89b-12d3-a456-426614174001', '550e8400-e29b-41d4-a716-446655440004', '[{"seat_id": "B2"}, {"seat_id": "B3"}]'),
    ('123e4567-e89b-12d3-a456-426614174000', '550e8400-e29b-41d4-a716-446655440005', '[{"seat_id": "C2"}, {"seat_id": "C3"}]'),
    ('123e4567-e89b-12d3-a456-426614174001', '550e8400-e29b-41d4-a716-446655440006', '[{"seat_id": "D1"}, {"seat_id": "D3"}]'),
    ('123e4567-e89b-12d3-a456-426614174000', '550e8400-e29b-41d4-a716-446655440007', '[{"seat_id": "E2"}, {"seat_id": "E4"}]'),
    ('123e4567-e89b-12d3-a456-426614174001', '550e8400-e29b-41d4-a716-446655440008', '[{"seat_id": "F1"}, {"seat_id": "F2"}]');

CREATE INDEX idx_reservations_user_id ON reservations(user_id);
CREATE INDEX idx_reservations_showtime_id ON reservations(showtime_id);
CREATE INDEX idx_reservations_reserved_seats ON reservations USING GIN (reserved_seats);

CREATE OR REPLACE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_reservations_updated_at
BEFORE UPDATE ON reservations
FOR EACH ROW
EXECUTE FUNCTION update_updated_at();

SELECT * FROM reservations;
