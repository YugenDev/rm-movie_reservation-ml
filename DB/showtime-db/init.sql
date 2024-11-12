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
    ('550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440003', '2023-10-02 14:00:00', 120, 15),
    ('550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440004', '2023-10-02 16:30:00', 180, 25),
    ('550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440005', '2023-10-02 19:00:00', 220, 35),
    ('550e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440006', '2023-10-03 14:00:00', 130, 10),
    ('550e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440007', '2023-10-03 16:00:00', 150, 50),
    ('550e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440008', '2023-10-03 18:30:00', 160, 75);


INSERT INTO seats (showtime_id, seat_number, is_reserved)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440003', 'A1', TRUE),
    ('550e8400-e29b-41d4-a716-446655440003', 'A2', FALSE),
    ('550e8400-e29b-41d4-a716-446655440003', 'A3', TRUE),
    ('550e8400-e29b-41d4-a716-446655440004', 'B1', FALSE),
    ('550e8400-e29b-41d4-a716-446655440004', 'B2', TRUE),
    ('550e8400-e29b-41d4-a716-446655440004', 'B3', TRUE),
    ('550e8400-e29b-41d4-a716-446655440004', 'B4', FALSE),
    ('550e8400-e29b-41d4-a716-446655440005', 'C1', FALSE),
    ('550e8400-e29b-41d4-a716-446655440005', 'C2', TRUE),
    ('550e8400-e29b-41d4-a716-446655440005', 'C3', TRUE),
    ('550e8400-e29b-41d4-a716-446655440005', 'C4', FALSE),
    ('550e8400-e29b-41d4-a716-446655440006', 'D1', TRUE),
    ('550e8400-e29b-41d4-a716-446655440006', 'D2', FALSE),
    ('550e8400-e29b-41d4-a716-446655440006', 'D3', TRUE),
    ('550e8400-e29b-41d4-a716-446655440007', 'E1', FALSE),
    ('550e8400-e29b-41d4-a716-446655440007', 'E2', TRUE),
    ('550e8400-e29b-41d4-a716-446655440007', 'E3', FALSE),
    ('550e8400-e29b-41d4-a716-446655440007', 'E4', TRUE),
    ('550e8400-e29b-41d4-a716-446655440008', 'F1', TRUE),
    ('550e8400-e29b-41d4-a716-446655440008', 'F2', TRUE),
    ('550e8400-e29b-41d4-a716-446655440008', 'F3', FALSE),
    ('550e8400-e29b-41d4-a716-446655440008', 'F4', FALSE);