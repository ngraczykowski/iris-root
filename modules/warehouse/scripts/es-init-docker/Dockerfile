FROM debian:11

RUN apt update -y && apt install -y curl && useradd app && mkdir /app && chown app /app

COPY --chown=app *.sh /app/
COPY --chown=app *.json /app/

USER app

CMD ["/app/init.sh"]
