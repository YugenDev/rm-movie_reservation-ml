import jwt
from datetime import datetime, timedelta
from config.settings import JWT_SECRET_KEY, JWT_ALGORITHM
from passlib.context import CryptContext


def create_access_token(user_id: str, role: str, expires_delta: timedelta = timedelta(hours=1)):
    to_encode = {
        "sub": user_id,
        "role": role,
        "exp": datetime.utcnow() + expires_delta
    }
    return jwt.encode(to_encode, JWT_SECRET_KEY, algorithm=JWT_ALGORITHM)


def verify_access_token(token: str):
    try:
        payload = jwt.decode(token, JWT_SECRET_KEY, algorithms=[JWT_ALGORITHM])
        return payload
    except jwt.ExpiredSignatureError:
        return None  # Token expired
    except jwt.InvalidTokenError:
        return None  # Invalid token


pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


def get_password_hash(password: str) -> str:
    return pwd_context.hash(password)


def verify_password(plain_password: str, hashed_password: str) -> bool:
    return pwd_context.verify(plain_password, hashed_password)