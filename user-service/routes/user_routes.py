from fastapi import APIRouter, Depends, HTTPException
from pydantic import UUID4
from sqlalchemy.orm import Session
from typing import List
from schemas.user_schema import UserCreateSchema, UserUpdateSchema, UserResponseSchema
from services.user_service import UserService
from config.session import get_db


# TODO: Password hashing and jwt impl

router = APIRouter()
user_service = UserService()

@router.get("/", response_model=List[UserResponseSchema])
def get_users(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    return user_service.get_users(db, skip, limit)

@router.get("/{user_id}", response_model=UserResponseSchema)
def get_user(user_id: UUID4, db: Session = Depends(get_db)):
    user = user_service.get_user(db, user_id)
    if user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return user

@router.post("/", response_model=UserCreateSchema)
def create_user(user: UserCreateSchema, db: Session = Depends(get_db)):
    return user_service.create_user(db, user)

@router.put("/{user_id}", response_model=UserResponseSchema)
def update_user(user_id: UUID4, user: UserUpdateSchema, db: Session = Depends(get_db)):
    return user_service.update_user(db, user_id, user)

@router.delete("/{user_id}")
def delete_user(user_id: UUID4, db: Session = Depends(get_db)):
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
