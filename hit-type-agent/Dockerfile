# syntax=docker/dockerfile:1.2
FROM alpine:latest AS BUILD
COPY hit_type /app/hit_type
COPY temp_agent /app/temp_agent
COPY config /app/config
COPY pyproject.toml setup.py setup.cfg requirements.txt /app/

FROM python:3.7-slim
ARG PYPI_URL

ENV SENTRY_DSN="https://099289013ac54df68ec534283859c863@o1129201.ingest.sentry.io/6468398"

COPY --from=BUILD /app /app

RUN apt-get update \
    && cd /app \
    && pip install . --index-url="$PYPI_URL"

EXPOSE 9090
ENTRYPOINT ["python", "-m", "hit_type.main", "-c", "/app/config", "--grpc"]
