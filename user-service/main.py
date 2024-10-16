from fastapi import FastAPI
from config import settings

app = FastAPI(
    title = settings.PROJECT_NAME,
    version = "1.0.0",
    description = settings.DESCRIPTION
)

@app.get("/")
async def root():
    return {"message": "User service is up!!! :)"}
