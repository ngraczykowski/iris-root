#!/bin/bash --login

mkdir -p logs
pip install tox-conda
python -m tox