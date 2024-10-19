import enum
from sqlalchemy import Column, String, UUID, Enum
from config.session import Base


class UserRole(enum.Enum):
    admin = 'admin'
    user = 'user'


class User(Base):
    __tablename__ = 'users'

    user_id = Column(UUID, primary_key=True, index=True, unique=True, nullable=False, server_default='gen_random_uuid()')
    username = Column(String, unique=True)
    password_hash = Column(String, nullable=False)
    email = Column(String, unique=True)
    role = Column(Enum(UserRole), default=UserRole.user)
