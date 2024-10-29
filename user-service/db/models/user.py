from sqlalchemy import Column, String, Enum
from sqlalchemy.dialects.postgresql import UUID
from db.session import Base
import uuid
import enum


class UserRole(enum.Enum):
    admin = 'admin'
    user = 'user'


class User(Base):
    __tablename__ = 'users'

    user_id = Column(UUID(as_uuid=True), primary_key=True, index=True, unique=True, nullable=False, default=uuid.uuid4)
    username = Column(String, unique=True)
    password_hash = Column(String, nullable=False)
    email = Column(String, unique=True)
    role = Column(Enum(UserRole), default=UserRole.user)
