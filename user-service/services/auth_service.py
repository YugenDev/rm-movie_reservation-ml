from sqlalchemy.orm import Session
from passlib.context import CryptContext
from db.repositories.user_repository import UserRepository
from utils.jwt_helper import create_access_token
from api.v1.schemas.user_schema import UserCreate as UserCreateSchema, UserUpdate as UserUpdateSchema, UserResponse as UserResponseSchema
from api.v1.schemas.login_schema import LoginSchema as UserLoginSchema
from datetime import timedelta
from fastapi import HTTPException, status

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


class AuthService:
    def __init__(self):
        self.repository = UserRepository()

    def verify_password(self, plain_password, hashed_password):
        return pwd_context.verify(plain_password, hashed_password)

    def authenticate_user(self, db: Session, username: str, password: str):
        if not username or not password:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Username and password are required")

        user = self.repository.get_user_by_email(db, username)
        if user is None or not self.verify_password(password, user.password_hash):
            raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid credentials")

        return user

    def login(self, db: Session, login_data: UserLoginSchema):
        user = self.authenticate_user(db, login_data.email, login_data.password)
        # Generate JWT
        access_token_expires = timedelta(hours=1)
        access_token = create_access_token(
            str(user.user_id), str(user.role), expires_delta=access_token_expires
        )
        return {"access_token": access_token, "token_type": "bearer"}
