from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from api.v1.schemas.user_schema import UserCreate as UserCreateSchema, UserUpdate as UserUpdateSchema, UserResponse as UserResponseSchema
from api.v1.schemas.login_schema import LoginSchema, LoginResponse
from services.user_service import UserService
from utils.dependencies import get_current_user
from db.session import get_db


router = APIRouter()
user_service = UserService()


@router.get("/", response_model=List[UserResponseSchema])
def get_users(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    return user_service.get_users(db, skip, limit)


@router.get("/user/{user_id}", response_model=UserResponseSchema)
def get_user(user_id: str, db: Session = Depends(get_db)):
    user = user_service.get_user(db, user_id)
    if user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return user


@router.post("/register", response_model=UserResponseSchema)
def register_user(user: UserCreateSchema, db: Session = Depends(get_db)):
    return user_service.create_user(db, user)


@router.post("/login", response_model=LoginResponse)
def login(credentials: LoginSchema, db: Session = Depends(get_db)):
    return user_service.login(db, credentials)


@router.put("/update/{user_id}", response_model=UserResponseSchema)
def update_user(user_id: str, user: UserUpdateSchema, db: Session = Depends(get_db)):
    return user_service.update_user(db, user_id, user)


@router.delete("/delete/{user_id}")
def delete_user(
    user_id: str,
    db: Session = Depends(get_db),
    current_user: UserResponseSchema = Depends(get_current_user)
):
    if str(current_user.user_id) != user_id and current_user.role != "admin":
        raise HTTPException(
            status_code=403,
            detail="You do not have permission to delete this user."
        )

    user_service = UserService()
    user_service.delete_user(db, user_id)
    return {"message": "User deleted successfully"}


@router.get("/email/{email}", response_model=UserResponseSchema)
def get_user_by_email(email: str, db: Session = Depends(get_db)):
    user = user_service.get_user_by_email(db, email)
    if user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return user


@router.get("/username/{username}", response_model=UserResponseSchema)
def get_user_by_username(username: str, db: Session = Depends(get_db)):
    user = user_service.get_user_by_username(db, username)
    if user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return user
