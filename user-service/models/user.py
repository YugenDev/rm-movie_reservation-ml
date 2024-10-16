from sqlalchemy import Column, String, UUID
from config.session import Base

class User(Base):
    __tablename__ = 'users'

    user_id = Column(UUID, primary_key=True)
    username = Column(String, unique=True)
    password_hash = Column(String)
    email = Column(String, unique=True)
    role = Column(String, enumerate=['admin', 'user'])
