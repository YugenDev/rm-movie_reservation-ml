CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE IF NOT EXISTS showtimes (
    showtime_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    movie_id UUID NOT NULL,
    showtime_date TIMESTAMP NOT NULL,
    capacity INTEGER NOT NULL CHECK (capacity >= 0),
    reserved_seats INTEGER NOT NULL DEFAULT 0 CHECK (reserved_seats >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS seats (
    seat_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    showtime_id UUID NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    is_reserved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_showtime 
        FOREIGN KEY (showtime_id) 
        REFERENCES showtimes(showtime_id) 
        ON DELETE CASCADE,
    CONSTRAINT unique_seat_per_showtime 
        UNIQUE(showtime_id, seat_number)
);


CREATE INDEX IF NOT EXISTS idx_showtimes_movie_id ON showtimes(movie_id);
CREATE INDEX IF NOT EXISTS idx_seats_showtime_id ON seats(showtime_id);


CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';


DROP TRIGGER IF EXISTS update_showtimes_updated_at ON showtimes;
CREATE TRIGGER update_showtimes_updated_at
    BEFORE UPDATE ON showtimes
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_seats_updated_at ON seats;
CREATE TRIGGER update_seats_updated_at
    BEFORE UPDATE ON seats
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();


INSERT INTO showtimes (showtime_id, movie_id, showtime_date, capacity, reserved_seats)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000', '2023-10-01 14:00:00', 100, 10),
    ('550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', '2023-10-01 16:00:00', 150, 20),
    ('550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002', '2023-10-01 18:00:00', 200, 30);

INSERT INTO seats (showtime_id, seat_number, is_reserved)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440000', 'A1', TRUE),
    ('550e8400-e29b-41d4-a716-446655440000', 'A2', FALSE),
    ('550e8400-e29b-41d4-a716-446655440001', 'B1', TRUE),
    ('550e8400-e29b-41d4-a716-446655440001', 'B2', FALSE),
    ('550e8400-e29b-41d4-a716-446655440002', 'C1', TRUE),
    ('550e8400-e29b-41d4-a716-446655440002', 'C2', FALSE);