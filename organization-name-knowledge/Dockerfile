# syntax=docker/dockerfile:1.2
FROM python:3.7-slim

RUN mkdir /app
WORKDIR /app
COPY organization_name_knowledge /app/organization_name_knowledge/
COPY pyproject.toml setup.py setup.cfg requirements.txt /app/

RUN apt-get update

RUN --mount=type=secret,id=mysecret\
     pip install .\
    --index-url="$(cat /run/secrets/mysecret)"

EXPOSE 5000
ENTRYPOINT ["python", "-m", "organization_name_knowledge", "--port", "5000"]