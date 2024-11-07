# User microservice 👥

## Install dependencies
By default the project uses poetry
```bash
poetry install
```

## Run
(This project uses environment variables in order to get the database and message broker connections)
```bash
poetry run uvicorn main:app --reload
```

## Environment variables
```bash
./.env

POSTGRES_USER= your_db_user
POSTGRES_PASSWORD= your_db_password
POSTGRES_DB= your_db_name
POSTGRES_SERVER= your_db_server
POSTGRES_PORT= your_db_port 
JWT_SECRET_KEY= your_secret_key
JWT_ALGORITHM= your_jwt_algorithm
RABBITMQ_SERVER= your_rabbitmq_server
RABBITMQ_PORT= your_rabbirmq_port
RABBITMQ_USER= your_rabbitmq_user
RABBITMQ_PASSWORD= your_rabbit_mq_password
```
If there is no .env file it will use some default values, take a look at the in the config module.