# syntax=docker/dockerfile:1.2
FROM alpine:latest AS BUILD
COPY company_name_surrounding /app/company_name_surrounding
COPY config /app/config
COPY pyproject.toml setup.py setup.cfg requirements.txt /app/

FROM python:3.7-slim
ARG PYPI_URL

ENV SENTRY_DSN="https://0838e91130ed4c9eae71f81d86e75589@o1129201.ingest.sentry.io/6468420"

COPY --from=BUILD /app /app

RUN apt-get update \
    && cd /app \
    && pip install . --index-url="$PYPI_URL"

EXPOSE 9090
ENTRYPOINT ["python", "-m", "company_name_surrounding", "-c", "/app/config", "--grpc"]
