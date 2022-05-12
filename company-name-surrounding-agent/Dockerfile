# syntax=docker/dockerfile:1.2
FROM python:3.7-slim

RUN mkdir /app
WORKDIR /app
COPY company_name_surrounding /app/company_name_surrounding/
COPY pyproject.toml setup.py setup.cfg requirements.txt /app/
COPY config /app/config/

RUN apt-get update

RUN --mount=type=secret,id=pypi_url_secret\
     pip install .\
    --index-url="$(cat /run/secrets/pypi_url_secret)"

EXPOSE 9090
CMD ["python", "-m", "company_name_surrounding", "-c", "/app/config", "--grpc", "-v"]
