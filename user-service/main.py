from fastapi import FastAPI
from config import settings
from db.session import engine, Base
from api.v1.routers import user_routes, auth_routes


app = FastAPI(
    title=settings.PROJECT_NAME,
    version="1.0.0",
    description=settings.DESCRIPTION
)


@app.get("/", tags=["healthcheck"])
async def root():
    return {"message": "User service is up!!! :)"}

Base.metadata.create_all(bind=engine)

app.include_router(user_routes.router, prefix="/users", tags=["users"])
app.include_router(auth_routes.router, prefix="/auth", tags=["auth"])

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
