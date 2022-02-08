FROM docker.repo.silenteight.com/etl-pipeline
SHELL ["conda", "run", "-n", "pipeline", "/bin/bash", "-c"]
RUN conda install -c conda-forge tox
SHELL ["exit"]
ADD . /app
WORKDIR /app
ENTRYPOINT ["conda", "run", "-n", "pipeline", "tox"]