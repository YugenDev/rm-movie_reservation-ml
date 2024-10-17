from sqlalchemy.orm import Session
from repositories.user_repository import UserRepository
from schemas.user_schema import UserCreateSchema, UserUpdateSchema


class UserService:
    def __init__(self):
        self.repository = UserRepository()
    
    def get_users(self, db: Session, skip: int = 0, limit: int = 100):
        return self.repository.get_users(db, skip, limit)
    
    def get_user(self, db: Session, user_id: str):
        return self.repository.get_user(db, user_id)
    
    def create_user(self, db: Session, user: UserCreateSchema):
        return self.repository.create_user(db, user)
    
    def update_user(self, db: Session, user_id: str, user: UserUpdateSchema):
        return self.repository.update_user(db, user_id, user)
    
    def delete_user(self, db: Session, user_id: str):
        self.repository.delete_user(db, user_id)

    def get_user_by_email(self, db: Session, email: str):
        return self.repository.get_user_by_email(db, email)
    
    def get_user_by_username(self, db: Session, username: str):
        return self.repository.get_user_by_username(db, username)  
