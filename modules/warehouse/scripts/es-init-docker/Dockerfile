FROM debian:11

RUN apt update -y && apt install -y curl jq && useradd app && mkdir /app && chown app /app

COPY --chown=app *.sh /app/

USER app

CMD ["/app/init.sh"]
