FROM docker.repo.silenteight.com/ds-anaconda:0.0.3-dev

ARG PIP_INDEX_URL
ENV PIP_INDEX_URL=$PIP_INDEX_URL

ADD requirements.txt /requirements.txt
ADD pipeline.yaml /pipeline.yaml
RUN conda install -c anaconda python=3.7
RUN conda env create  -f /pipeline.yaml
RUN apt install git -y
SHELL ["conda", "run", "-n", "pipeline", "/bin/bash", "-c"]
RUN mkdir /server_entrypoint
ADD scripts/start.sh /server_entrypoint/start.sh
RUN chmod +x /server_entrypoint/start.sh
RUN conda clean --all --force-pkgs-dirs -y
RUN conda install python
RUN rm /requirements.txt /pipeline.yaml
WORKDIR /app

SHELL ["exit"]
ENTRYPOINT ["/server_entrypoint/start.sh"]
