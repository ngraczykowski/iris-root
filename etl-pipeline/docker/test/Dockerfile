FROM docker.repo.silenteight.com/etl-pipeline
ADD . /app
WORKDIR /app
SHELL ["conda", "run", "-n", "pipeline", "/bin/bash", "-c"]
RUN conda install -c conda-forge tox
SHELL ["exit"]
ENTRYPOINT ["/bin/sh"]
