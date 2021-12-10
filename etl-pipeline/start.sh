#!/bin/bash --login
set -e

# activate conda environment and let the following process take over
sh -c "conda init bash"
conda activate pipeline
exec "$@"
