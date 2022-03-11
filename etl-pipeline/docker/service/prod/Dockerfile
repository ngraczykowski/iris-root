FROM python:3.7-slim
ARG PIP_INDEX_URL
ENV PIP_INDEX_URL=$PIP_INDEX_URL
RUN apt-get update  && apt-get install -y wget
RUN mkdir /app
WORKDIR /app
ADD . /app/
RUN pip install .
EXPOSE 9090
CMD ["python", "-m", "etl_pipeline"]
