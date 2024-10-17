from pydantic import BaseModel, UUID4
from typing import Optional


class UserBaseSchema(BaseModel):
    username: str
    email: str
    role: Optional[str] = "user"


class UserCreateSchema(UserBaseSchema):
    password: str


class UserSchema(UserBaseSchema):
    user_id: UUID4
    password: str


class UserLoginSchema(BaseModel):
    username: str
    password: str


class UserUpdateSchema(UserBaseSchema):
    user_id: UUID4
    password: str


class UserResponseSchema(UserBaseSchema):
    user_id: UUID4
    pass


class UserLoginResponseSchema(BaseModel):
    username: str
    role: str
    token: str


class UserUpdateResponseSchema(UserBaseSchema):
    pass


class UserListResponseSchema(UserBaseSchema):
    pass


class UserDeleteResponseSchema(UserBaseSchema):
    pass
