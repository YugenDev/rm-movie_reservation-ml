from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from api.v1.schemas.user_schema import UserCreate as UserCreateSchema, UserResponse as UserResponseSchema
from api.v1.schemas.login_schema import LoginSchema
from services.auth_service import AuthService
from db.session import get_db

router = APIRouter()
auth_service = AuthService()


@router.post("/login")
def login(credentials: LoginSchema, db: Session = Depends(get_db)):
    return auth_service.login(db, credentials)
