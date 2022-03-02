#!/bin/bash --login

echo ======= Notebook launched ========
mkdir -p logs
conda run -n pipeline jupyter lab --allow-root --ip 0.0.0.0 --NotebookApp.token='' > logs/out.log &> logs/error.log
