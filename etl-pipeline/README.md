# ETL pipeline

Generic project that help Data Scientists in analyze of the Client's data as well as Developers in creation of fast and scalable architecture of microservices. 

# MVP

1. Checkout to this branch `at/basic-mvp`
3. Load submodules: `git submodule update --init --recursive`
2. Add your key to SE repos:
` export PIP_INDEX_URL=https://<username>:<API_KEY>@repo.silenteight.com/artifactory/api/pypi/pypi/simple`
or place it in `docker/docker-compose.yaml` file everywhere on `PIP_INDEX_URL=https://<username>:<API_KEY>@repo.silenteight.com/artifactory/api/pypi/pypi/simple`
3. Launch images
```
docker-compose -f docker/docker-compose.yaml up
```

4. Take a cup of coffee and wait ~5-10 minutes
5. If a log "======= Notebook launched ========" occurs, please go to `localhost:4444` in your browser and explore Data Platform - MVP.ipynb. 

Required output:
```
Attaching to docker_java_playground_1, docker_anaconda_playground_1, docker_jupyter_playground_1
jupyter_playground_1   | ======= Notebook launched ========
docker_java_playground_1 exited with code 0
docker_anaconda_playground_1 exited with code 0
```