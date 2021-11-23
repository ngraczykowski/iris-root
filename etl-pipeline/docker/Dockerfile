FROM ubuntu:latest
RUN apt update -y
RUN apt-get install software-properties-common -y --fix-missing
# scripts/download-and-upload.sh local
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
RUN scripts/download-and-upload.sh local
RUN chmod +x /env/installer/ds-installer-1.0.26.run
RUN /env/installer/ds-installer-1.0.26.run --target /env/ds

CMD ["/bin/sh"]