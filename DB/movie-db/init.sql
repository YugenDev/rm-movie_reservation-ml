CREATE TABLE movies (
    movie_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    poster_image_url VARCHAR(255),
    genre VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO movies (title, description, poster_image_url, genre)
VALUES
    ('El Último Amanecer', 'Una épica aventura post-apocalíptica donde los pocos sobrevivientes luchan por reconstruir la civilización.', 'https://example.com/posters/ultimo_amanecer.jpg', 'Ciencia Ficción'),
    ('Rostros en la Niebla', 'Un thriller psicológico donde un detective investiga una serie de desapariciones misteriosas en una ciudad envuelta en niebla.', 'https://example.com/posters/rostros_niebla.jpg', 'Suspenso'),
    ('Viaje a la Eternidad', 'Una película que explora el concepto de la vida después de la muerte a través de los ojos de un joven que busca respuestas.', 'https://example.com/posters/viaje_eternidad.jpg', 'Drama'),
    ('Fuerzas del Universo', 'Un grupo de héroes debe enfrentar una invasión alienígena mientras luchan por salvar su hogar y a sus seres queridos.', 'https://example.com/posters/fuerzas_universo.jpg', 'Acción'),
    ('El Bosque Prohibido', 'Un cuento de terror que sigue a un grupo de amigos que se adentran en un bosque oscuro, donde se desatan fuerzas sobrenaturales.', 'https://example.com/posters/bosque_prohibido.jpg', 'Terror'),
    ('Corazón de Hierro', 'Un drama histórico sobre la vida de un soldado en la Segunda Guerra Mundial que debe hacer frente a sus miedos más profundos.', 'https://example.com/posters/corazon_jerro.jpg', 'Bélica');
