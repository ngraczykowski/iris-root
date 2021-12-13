#!/bin/bash --login
echo ======= Notebook launched ========
conda run -n pipeline jupyter notebook --allow-root --ip 0.0.0.0 --NotebookApp.token='' > logs/out.log &> logs/error.log
