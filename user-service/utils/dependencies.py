from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.orm import Session
from utils.jwt_helper import verify_access_token
from db.session import get_db
from db.repositories.user_repository import UserRepository


oauth2_scheme = OAuth2PasswordBearer(tokenUrl="login")


def get_current_user(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    payload = verify_access_token(token)
    if payload is None:
        raise credentials_exception
    user = UserRepository().get_user(db, user_id=payload.get("sub"))
    if user is None:
        raise credentials_exception
    return user


def get_current_user_with_role(required_role: str):
    def role_checker(token: str = Depends(oauth2_scheme)):
        payload = verify_access_token(token)
        if not payload or payload.get("role") != required_role:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail="You do not have access to this resource",
                headers={"WWW-Authenticate": "Bearer"},
            )
        return payload
    return role_checker
