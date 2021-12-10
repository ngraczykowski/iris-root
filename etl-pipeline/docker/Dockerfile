FROM anaconda_playground
ARG PIP_INDEX_URL
ENV PIP_INDEX_URL=$PIP_INDEX_URL
ADD requirements.txt /requirements.txt
ADD pipeline.yaml /pipeline.yaml
ADD pip-requirements.txt  /pip-requirements.txt

RUN conda env create  -f /pipeline.yaml python=3.7
RUN export PIP
# RUN which pip
# RUN mkdir -p /opt/jdk/
# ARG PIP_INDEX_URL
# ENV PIP_INDEX_URL=$PIP_INDEX_URL
# ADD requirements.txt /requirements.txt
# RUN pip install -r /requirements.txt
# ADD pip-requirements.txt  /pip-requirements.txt
# RUN pip install -r /pip-requirements.txt
# RUN pip install pyspark==3.1.1
SHELL ["conda", "run", "-n", "pipeline", "/bin/bash", "-c"]

RUN conda install jupyter
SHELL ["exit"]

WORKDIR /app
ENTRYPOINT [ "conda", "run", "-n", "pipeline", "jupyter", "notebook", "--allow-root", "--ip", "0.0.0.0", "--NotebookApp.token=''"]
