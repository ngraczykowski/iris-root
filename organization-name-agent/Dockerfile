# syntax=docker/dockerfile:1.2
FROM python:3.7-slim

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
CMD ["python", "-m", "company_name.main", "-c", "/app/config", "--grpc", "-v"]
