from sqlalchemy.orm import Session
from sqlalchemy.exc import SQLAlchemyError
from fastapi import HTTPException, status
from models.user import User
from api.v1.schemas.user_schema import UserCreate as UserCreateSchema, UserUpdate as UserUpdateSchema, UserResponse as UserResponseSchema
from utils.jwt_helper import get_password_hash


class UserRepository:
    def get_users(self, db: Session, skip: int, limit: int) -> list[UserResponseSchema]:
        return db.query(User).offset(skip).limit(limit).all()

    def get_user(self, db: Session, user_id: str):
        user = db.query(User).filter(User.user_id == user_id).first()
        if not user:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
        return user

    def create_user(self, db: Session, user: UserCreateSchema) -> UserResponseSchema:
        if db.query(User).filter(User.email == user.email).first():
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Email already registered")
        if db.query(User).filter(User.username == user.username).first():
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Username already taken")
        if not user.email or not user.username or not user.password:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Missing required fields")
        if len(user.password) < 8:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Password must be at least 8 characters long")
        try:

            hashed_password = get_password_hash(user.password)

            new_user = User(
                username=user.username,
                email=user.email,
                password_hash=hashed_password,
                role=user.role
            )
            db.add(new_user)
            db.commit()
            db.refresh(new_user)
            return UserResponseSchema(
                user_id=str(new_user.user_id),
                username=new_user.username,
                email=new_user.email,
                role=new_user.role
            )
        except SQLAlchemyError as e:
            db.rollback()
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(e))

    def update_user(self, db: Session, user_id: str, user: UserUpdateSchema):
        existing_user = db.query(User).filter(User.user_id == user_id).first()
        if not existing_user:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
        if user.email and db.query(User).filter(User.email == user.email, User.user_id != user_id).first():
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Email already registered")
        if user.username and db.query(User).filter(User.username == user.username, User.user_id != user_id).first():
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Username already taken")
        try:
            for key, value in user.model_dump().items():
                setattr(existing_user, key, value)
            db.commit()
            db.refresh(existing_user)
            return existing_user
        except SQLAlchemyError as e:
            db.rollback()
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(e))

    def delete_user(self, db: Session, user_id: str):
        user = db.query(User).filter(User.user_id == user_id).first()
        if not user:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
        try:
            db.delete(user)
            db.commit()
        except SQLAlchemyError as e:
            db.rollback()
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(e))

    def get_user_by_email(self, db: Session, email: str):
        user = db.query(User).filter(User.email == email).first()
        if not user:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
        return user

    def get_user_by_username(self, db: Session, username: str):
        user = db.query(User).filter(User.username == username).first()
        if not user:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
        return user
