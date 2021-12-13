# ETL pipeline

Generic project that help Data Scientists in analyze of the Client's data as well as Developers in creation of fast and scalable architecture of microservices. 

# MVP

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