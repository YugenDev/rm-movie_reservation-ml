FROM postgres:17.0-alpine3.20

ENV POSTGRES_DB reservations
ENV POSTGRES_USER admin
ENV POSTGRES_PASSWORD admin123

COPY ./init.sql /docker-entrypoint-initdb.d/

CMD [ "postgres" ]