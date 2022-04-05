# syntax=docker/dockerfile:1.2
FROM alpine:latest AS BUILD
COPY company_name /app/company_name
COPY config /app/config
COPY pyproject.toml setup.py setup.cfg requirements.txt /app/
RUN mv /app/config/application.local.yaml /app/config/application.yaml

FROM python:3.7-slim
ARG PYPI_URL

ENV SENTRY_DSN="https://7369e7facb1b478cbb1b63a9d445291b@o1129201.ingest.sentry.io/6190596"

COPY --from=BUILD /app /app

RUN apt-get update \
    && cd /app \
    && pip install . --index-url="$PYPI_URL"

EXPOSE 9090
ENTRYPOINT ["python", "-m", "company_name.main", "-c", "/app/config", "--grpc"]
