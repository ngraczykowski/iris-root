FROM docker.repo.silenteight.com/ds-anaconda

ARG PIP_INDEX_URL
ENV PIP_INDEX_URL=$PIP_INDEX_URL

ADD requirements.txt /requirements.txt
ADD pipeline.yaml /pipeline.yaml

RUN conda env create  -f /pipeline.yaml python=3.7
RUN apt install make -y
SHELL ["conda", "run", "-n", "pipeline", "/bin/bash", "-c"]
RUN conda install jupyter
RUN mkdir /server_entrypoint
ADD start.sh /server_entrypoint/start.sh
RUN chmod +x /server_entrypoint/start.sh
WORKDIR /app
SHELL ["exit"]

ENTRYPOINT ["/server_entrypoint/start.sh"]
