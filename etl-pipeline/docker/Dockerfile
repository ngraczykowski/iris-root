FROM ubuntu:20.04 as os_system
RUN apt update -y
RUN apt-get install software-properties-common -y --fix-missing
ADD scripts /scripts
ENV ROOT_DIR="/"
RUN apt install wget unzip curl -y

FROM os_system as os_system_with_java
RUN mkdir -p /opt/jdk/
COPY datascience-installer/dependencies/bellsoft-jdk11.0.10+9-linux-amd64.tar.gz /opt/jdk/
RUN tar xf /opt/jdk/bellsoft-jdk11.*-linux-amd64.tar.gz --strip-components 1 -C /opt/jdk
RUN rm /opt/jdk/bellsoft-jdk11.*-linux-amd64.tar.gz

FROM os_system
RUN mkdir -p /opt/jdk/
COPY --from=os_system_with_java /opt/jdk /opt/