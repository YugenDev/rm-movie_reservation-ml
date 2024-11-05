from pydantic import BaseModel, EmailStr
from enum import Enum


class UserRole(str, Enum):
    admin = 'admin'
    user = 'user'


class LoginSchema(BaseModel):
    email: EmailStr
    password: str
