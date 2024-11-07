from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.exc import OperationalError
from sqlalchemy import create_engine, text
from tenacity import retry, wait_fixed, stop_after_attempt
from config.settings import DATABASE_URL, DB_POOL_SIZE, DB_MAX_OVERFLOW, DB_POOL_RECYCLE, DB_POOL_TIMEOUT


@retry(wait=wait_fixed(10), stop=stop_after_attempt(5))
def create_engine_with_retries(uri):
    engine = create_engine(
        uri,
        pool_size=DB_POOL_SIZE,
        max_overflow=DB_MAX_OVERFLOW,
        pool_recycle=DB_POOL_RECYCLE,
        pool_timeout=DB_POOL_TIMEOUT
    )
    try:
        with engine.connect() as connection:
            result = connection.execute(text("SELECT 1"))
            if result.fetchone() is None:
                raise OperationalError("Consulta de conexión fallida")
    except OperationalError as e:
        print(f'Error al conectar a la base de datos: {e}')
        raise
    return engine


engine = create_engine_with_retries(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
