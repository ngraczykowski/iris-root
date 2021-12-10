FROM anaconda_playground
RUN conda create -n pipeline python=3.7
RUN conda activate anaconda_playground
RUN mkdir -p /opt/jdk/
ARG PIP_INDEX_URL
ENV PIP_INDEX_URL=$PIP_INDEX_URL
RUN pip install -r requirements.txt 
RUN pip install -r python-datatoolbox/pip-requirements.txt 
RUN pip install pyspark==3.1.1
RUN conda install jupyter
WORKDIR /app
ENV PYTHONPATH=.:python-datatoolbox
ENTRYPOINT [ "python", "-m", "notebook", "--allow-root", "--ip",  "0.0.0.0' ]
