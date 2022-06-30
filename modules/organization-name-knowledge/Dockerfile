# syntax=docker/dockerfile:1.2
FROM alpine:latest AS BUILD

COPY organization_name_knowledge /app/organization_name_knowledge
COPY pyproject.toml setup.py setup.cfg requirements.txt /app/

FROM python:3.7-slim
ARG PYPI_URL

COPY --from=BUILD /app /app

RUN apt-get update \
    && cd /app \
    && pip install . --index-url="$PYPI_URL"

EXPOSE 5000
ENTRYPOINT ["python", "-m", "organization_name_knowledge", "--port", "5000"]
