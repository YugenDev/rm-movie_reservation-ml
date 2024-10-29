from sqlalchemy.orm import Session
from passlib.context import CryptContext
from db.repositories.user_repository import UserRepository
from utils.jwt_helper import create_access_token
from api.v1.schemas.user_schema import UserCreate as UserCreateSchema, UserUpdate as UserUpdateSchema, UserResponse as UserResponseSchema
from api.v1.schemas.login_schema import LoginSchema as UserLoginSchema
from utils.rabbitmq import RabbitMQConnection
from config.settings import RABBITMQ_SERVER, RABBITMQ_PORT, RABBITMQ_USER, RABBITMQ_PASSWORD
from datetime import timedelta


pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


class UserService:
    def __init__(self):
        self.repository = UserRepository()
        self.rabbitmq = RabbitMQConnection(
            host=RABBITMQ_SERVER,
            port=RABBITMQ_PORT,
            user=RABBITMQ_USER,
            password=RABBITMQ_PASSWORD
        )

    def verify_password(self, plain_password, hashed_password):
        return pwd_context.verify(plain_password, hashed_password)

    def get_users(self, db: Session, skip: int = 0, limit: int = 100):
        return self.repository.get_users(db, skip, limit)

    def get_user(self, db: Session, user_id: str):
        return self.repository.get_user(db, user_id)

    def create_user(self, db: Session, user: UserCreateSchema):
        created_user = self.repository.create_user(db, user)

        # Connect to rabbitMQ and send a messagge to the queue
        self.rabbitmq.connect()
        self.rabbitmq.declare_queue(queue_name='user_notifications')
        self.rabbitmq.publish_message(queue_name='user_notifications', message=f'User {created_user.user_id} created')
        self.rabbitmq.close()

        return created_user

    def authenticate_user(self, db: Session, username: str, password: str):
        user = self.repository.get_user_by_username(db, username)
        if not user:
            return None
        if not self.verify_password(password, user.password_hash):
            return None
        return user

    def login(self, db: Session, login_data: UserLoginSchema):
        user = self.authenticate_user(db, login_data.username, login_data.password)
        if not user:
            return None  # Credenciales inválidas
        # Generate JWT
        access_token_expires = timedelta(hours=1)
        access_token = create_access_token(
            str(user.user_id), str(user.role), expires_delta=access_token_expires
        )
        return {"access_token": access_token, "token_type": "bearer"}

    def update_user(self, db: Session, user_id: str, user: UserUpdateSchema):
        return self.repository.update_user(db, user_id, user)

    def delete_user(self, db: Session, user_id: str):
        self.repository.delete_user(db, user_id)

    def get_user_by_email(self, db: Session, email: str):
        return self.repository.get_user_by_email(db, email)

    def get_user_by_username(self, db: Session, username: str):
        return self.repository.get_user_by_username(db, username)
