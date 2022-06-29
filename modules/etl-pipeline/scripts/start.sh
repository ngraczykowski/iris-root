#!/bin/bash --login

echo ======= Notebook launched ========
mkdir -p logs
/env/ds/anaconda/envs/pipeline/bin/python -m jupyter notebook --allow-root --ip '0.0.0.0' --NotebookApp.token='' > logs/out.log &> logs/error.log
