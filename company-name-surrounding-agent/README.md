# Company Name Surrounding Agent

Rules specified in application.yaml, are checked from top to bottom, if any range met - return its solution.
If no range met, returns the 'default response' 
Installing:

```pip install -e .```

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
