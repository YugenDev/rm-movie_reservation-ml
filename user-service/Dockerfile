FROM python:3.13.0-alpine

WORKDIR /app

RUN apk add --no-cache --virtual .build-deps \
    gcc libc-dev libffi-dev \
    && pip install --no-cache-dir poetry \
    && apk del .build-deps

COPY pyproject.toml poetry.lock* ./

RUN poetry install --no-root

COPY . .

EXPOSE 8000

CMD ["poetry", "run", "gunicorn", "-w", "4", "-k", "uvicorn.workers.UvicornWorker", "--bind", "[::]:8000", "main:app"]
