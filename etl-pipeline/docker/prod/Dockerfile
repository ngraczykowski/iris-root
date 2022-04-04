FROM docker.repo.silenteight.com/ds-anaconda:0.0.3-dev

ARG PIP_INDEX_URL
ENV PIP_INDEX_URL=$PIP_INDEX_URL


ADD pipeline_for_playground.yaml /pipeline.yaml
RUN conda install -c anaconda python=3.6
RUN apt-get update --fix-missing
RUN apt install git gcc -y
RUN conda env create  -f /pipeline.yaml
SHELL ["conda", "run", "-n", "pipeline", "/bin/bash", "-c"]
RUN mkdir /server_entrypoint
ADD scripts/start.sh /server_entrypoint/start.sh
RUN chmod +x /server_entrypoint/start.sh
RUN conda clean --all --force-pkgs-dirs -y
RUN conda install python
RUN conda install jupyter
 
ADD minimal_requirements.txt /requirements.txt
RUN ls -l /
RUN ls -l /requirements.txt
RUN pip install -r /requirements.txt
WORKDIR /app

SHELL ["exit"]
ENTRYPOINT ["/bin/sh"]
