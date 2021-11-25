FROM ubuntu:latest
RUN apt update -y
RUN apt-get install software-properties-common -y --fix-missing
ARG PIP_INDEX_URL
ENV PIP_INDEX_URL=$PIP_INDEX_URL
RUN apt install python3-pip -y
ADD requirements.txt /requirements.txt
RUN pip3 install -r /requirements.txt
RUN apt install curl -y
RUN apt install wget -y
RUN apt install zip -y
ADD .env /.env
ADD scripts /scripts
ENV ROOT_DIR="/"

RUN mkdir -p /opt/jdk/
COPY datascience-installer/dependencies/bellsoft-jdk11.0.10+9-linux-amd64.tar.gz /opt/jdk/
RUN tar xf /opt/jdk/bellsoft-jdk11.*-linux-amd64.tar.gz --strip-components 1 -C /opt/jdk
EXPOSE 4040 7077
CMD ["/bin/sh"]
# export ROOT_DIR="$(cd -- "$(dirname -- "${0}")" && cd .. && pwd)"
# export SERP_HOME="${SERP_HOME:-$ROOT_DIR/env/serp}"
# export PATH="$ROOT_DIR/opt/jdk/bin:$SERP_HOME/bin:$ROOT_DIR/bin:$ROOT_DIR/opt/anaconda/bin:$PATH"