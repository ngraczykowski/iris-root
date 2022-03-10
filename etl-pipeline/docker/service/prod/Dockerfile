FROM python:3.7-slim

RUN mkdir /app
WORKDIR /app
ADD . /app/


RUN apt-get update  && apt-get install -y wget;
RUN pip install
EXPOSE 9090
CMD ["python", "/app/company_name.pyz", "-c", "/app/config", "--grpc", "-v"]
