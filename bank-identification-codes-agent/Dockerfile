# syntax=docker/dockerfile:1.2
FROM alpine:latest AS BUILD
COPY bank_identification_codes /app/bank_identification_codes
COPY config /app/config
COPY pyproject.toml setup.py setup.cfg requirements.txt /app/

FROM python:3.7-slim
ARG PYPI_URL

COPY --from=BUILD /app /app

RUN apt-get update \
    && cd /app \
    && pip install . --index-url="$PYPI_URL"

EXPOSE 9090
ENTRYPOINT ["python", "-m", "company_name.main", "-c", "/app/config", "--grpc"]
