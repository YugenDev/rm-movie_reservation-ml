from pydantic import BaseModel, EmailStr, constr
from enum import Enum


class UserRole(str, Enum):
    admin = 'admin'
    user = 'user'

class Login(BaseModel):
    email: EmailStr
    password: str

class LoginResponse(BaseModel):
    username: str
    role: UserRole
    access_token: str
    token_type: str