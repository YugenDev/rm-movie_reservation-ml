from fastapi import FastAPI
from config import settings
from config.session import engine, Base
from routes import user_routes


app = FastAPI(
    title=settings.PROJECT_NAME,
    version="1.0.0",
    description=settings.DESCRIPTION
)


@app.get("/healthcheck", tags=["healthcheck"])
async def root():
    return {"message": "User service is up!!! :)"}

Base.metadata.create_all(bind=engine)

app.include_router(user_routes.router, prefix="/users", tags=["users"])

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
