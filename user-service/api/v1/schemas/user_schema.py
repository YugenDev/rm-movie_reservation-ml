from pydantic import BaseModel, EmailStr, constr
from enum import Enum
from typing import Optional


class UserRole(str, Enum):
    admin = 'admin'
    user = 'user'

class UserCreate(BaseModel):
    username: constr(min_length=1, max_length=50)  
    password: constr(min_length=8)  
    email: EmailStr  
    role: Optional[UserRole] = UserRole.user

class UserUpdate(BaseModel):
    username: Optional[constr(min_length=1, max_length=50)]
    password: Optional[constr(min_length=8)]
    email: Optional[EmailStr]
    role: Optional[UserRole]

# Generic User response
class UserResponse(BaseModel):
    user_id: str
    username: str
    email: EmailStr
    role: UserRole

    class Config:
        from_attributes = True  # Allows pydantic to use SQLAlchemy models
