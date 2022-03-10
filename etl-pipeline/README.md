# ETL pipeline

Generic project that help Data Scientists in analyze of the Client's data as well as Developers in creation of fast and scalable architecture of microservices. 

# Service

1. Install etl service the newest version (it's >0.5.4!)"
`pip install etl_pipeline`

2. Copy paste directory `config` and set-up `config/service/service.yaml` settings
`CONFIG_APP_DIR=<PATH TO CONFIG DIRECTORY> python -m etl_pipeline`

## Emulating environment

1. You can launch service via:
`CONFIG_APP_DIR=etl_service/config python etl_pipeline`

2. You can launch data source service stub via:
`python tests/test_custom/data_source_stub.py`

3. You can now send request as shown in:
`pytest ./tests/test_custom/test_client.py`



# Playground

1. Load submodules: `git submodule update --init --recursive`
2. Docker login:
```
docker login docker.repo.silenteight.com
```
2. Pull image
```
docker pull docker.repo.silenteight.com/etl-pipeline
```

3. Launch:
```
docker-compose -f docker/docker-compose.yaml up
```
4. If a log "======= Notebook launched ========" occurs, please go to `localhost:4444` in your browser and explore Data Platform - MVP.ipynb. 

Required output:
```
Attaching to docker_jupyter_playground_1
jupyter_playground_1   | ======= Notebook launched ========
```

Output is saved to `data` directory in real time after each step. Offline results are also available in the `tests/data` directory. 