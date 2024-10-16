from pydantic import BaseModel

class UserBaseSchema(BaseModel):
    username: str
    email: str
    role: str

class UserSchema(UserBaseSchema):
    user_id: str
    password: str

class UserLoginSchema(BaseModel):
    username: str
    password: str

class UserUpdateSchema(UserBaseSchema):
    password: str

class UserResponseSchema(UserBaseSchema):
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
