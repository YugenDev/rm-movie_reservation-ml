CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS movies (
    movie_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),  
    description TEXT NOT NULL,
    poster_image_url VARCHAR(255),
    genre VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX IF NOT EXISTS idx_genre ON movies(genre);


CREATE INDEX IF NOT EXISTS idx_created_at ON movies(created_at);


CREATE INDEX IF NOT EXISTS idx_updated_at ON movies(updated_at);


CREATE OR REPLACE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER update_movies_updated_at
BEFORE UPDATE ON movies
FOR EACH ROW
EXECUTE FUNCTION update_updated_at();


INSERT INTO movies (title, description, poster_image_url, genre)
VALUES
    ('El Último Amanecer', 'Una épica aventura post-apocalíptica donde los pocos sobrevivientes luchan por reconstruir la civilización.', 'https://example.com/posters/ultimo_amanecer.jpg', 'Ciencia Ficción'),
    ('Rostros en la Niebla', 'Un thriller psicológico donde un detective investiga una serie de desapariciones misteriosas en una ciudad envuelta en niebla.', 'https://example.com/posters/rostros_niebla.jpg', 'Suspenso'),
    ('Viaje a la Eternidad', 'Una película que explora el concepto de la vida después de la muerte a través de los ojos de un joven que busca respuestas.', 'https://example.com/posters/viaje_eternidad.jpg', 'Drama'),
    ('Fuerzas del Universo', 'Un grupo de héroes debe enfrentar una invasión alienígena mientras luchan por salvar su hogar y a sus seres queridos.', 'https://example.com/posters/fuerzas_universo.jpg', 'Acción'),
    ('El Bosque Prohibido', 'Un cuento de terror que sigue a un grupo de amigos que se adentran en un bosque oscuro, donde se desatan fuerzas sobrenaturales.', 'https://example.com/posters/bosque_prohibido.jpg', 'Terror'),
    ('Corazón de Hierro', 'Un drama histórico sobre la vida de un soldado en la Segunda Guerra Mundial que debe hacer frente a sus miedos más profundos.', 'https://example.com/posters/corazon_jerro.jpg', 'Bélica');
