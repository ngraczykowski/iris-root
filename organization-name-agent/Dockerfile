# syntax=docker/dockerfile:1.2
FROM python:3.7-slim

ENV SENTRY_DSN="https://7369e7facb1b478cbb1b63a9d445291b@o1129201.ingest.sentry.io/6190596"

RUN mkdir /app
WORKDIR /app
COPY company_name /app/company_name/
COPY pyproject.toml setup.py setup.cfg requirements.txt /app/
COPY config /app/config/
RUN cp /app/config/application.local.yaml /app/config/application.yaml

RUN apt-get update

RUN --mount=type=secret,id=mysecret\
     pip install .\
    --index-url="$(cat /run/secrets/mysecret)"

EXPOSE 9090
ENTRYPOINT ["python", "-m", "company_name.main", "-c", "/app/config", "--grpc"]
