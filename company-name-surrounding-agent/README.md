# Company Name Surrounding Agent

The agent purpose is to count organization - name related tokens: legal terms, prefixes and suffixes.
Agent returns this number only when gets a list of length 1. Otherwise, returns 0. 
This number is returned as _result_. _solution_ is created based on rules provided in config.

Rules specified in _solution_rules_ field in  _con  fig/application.yaml_, are checked from top to bottom, 
If any range met - returns its solution. If no range met, returns the 'default response' 

## Installing:
It's recommended to use separate venv (virtual environment). To get one and activate it, run:
```
python -m venv new_virtual_env_name 
source new_virtual_env_name/bin/activate
```
### From source:

```pip install -e .```

### From S8 PyPi:

```pip install company-name-surrounding-agent```

## Running:
To run, no matter which way installed - it is required to provide an _application.yaml_ file with configuration 
for agent and solution parsing. If any of them is not provided, _ConfigurationException_ raises.
The _application.yaml_ file must be stored in a _config_ directory. If _config_ directory location is different 
from default (the same as the script is run in), it is possible to specify it in _configuration-dir_ run argument.
```
python -m company_name_surrounding [-h] [-c CONFIGURATION_DIR] [--grpc] [-v]

or, when using .pyz file:

python company_name_surrounding.pyz [-h] [-c CONFIGURATION_DIR] [--grpc] [-v]

optional arguments:
  -h, --help            show this help message and exit
  -c CONFIGURATION_DIR, --configuration-dir CONFIGURATION_DIR
                        Path for configuration files
  --grpc                Start grpc service
  -v, --verbose         Increase verbosity for debug purpose
```
