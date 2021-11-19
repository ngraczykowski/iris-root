# Company Name Surrounding Agent

The agent purpose is to count organization - name related tokens: legal terms, pre- and suffixes.
Agent returns this number only when gets a list of length 1. Otherwise, returns 0. 
This number is returned as Result's *count* field or grpc's *result*, 
and *solution* is created based on rules provided in config and returned as *solution* in both

Rules specified in application.yaml, are checked from top to bottom, if any range met - return its solution.
If no range met, returns the 'default response' 

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

Running:
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
