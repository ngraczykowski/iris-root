# ETL pipeline

Generic project that help Data Scientists in analyze of the Client's data as well as Developers in creation of fast and scalable architecture of microservices. 

# MVP

1. Checkout to this branch `at/basic-mvp`
2. Load submodules:

2. Add your key to SE repos:
` export PIP_INDEX_URL=https://<username>:<API_KEY>@repo.silenteight.com/artifactory/api/pypi/pypi/simple`
or place it in `docker/docker-compose.yaml` file everywhere on `PIP_INDEX_URL=https://<username>:<API_KEY>@repo.silenteight.com/artifactory/api/pypi/pypi/simple`
3. Launch images
`docker-compose -f docker/docker-compose.yaml up`
4. Take a cup of coffee and wait ~5-10 minutes
5. If a log "Notebook launched" occur go to `localhost:4444` in your browser and explore Data Platform - MVP.ipynb. 
```
cd spark-manager
git checkout at/spark_manager
git submodule init
git submodule update --recursive
docker-compose -f docker/docker-compose.yaml build
```

4. Build anaconda image 
```
cd spark-manager
git checkout at/spark_manager
git submodule init
git submodule update --recursive
docker-compose -f docker/docker-compose.yaml build
```

4. Build anaconda image 
```
cd spark-manager
git checkout at/spark_manager
git submodule init
git submodule update --recursive
docker-compose -f docker/docker-compose.yaml build
```