# agent base package

For ease of implementing functioning agents in python, 
this package should contain all common code for integration with other parts of the system.


## Installing

Installing from PyPI:
```bash
pip install agent-base
```

or from source
```bash
pip install .
```

or building wheel from source in dist directory
```bash
./scripts/bootstrap.sh
./scripts/prepare_artifacts.sh
```


## Implementing your agent

Sample dummy agent implementation can be found in `agent_base/example.py`. 
Sample agent implemented using this package is company name agent.

### Agent

The main method in your Agent implementation is `resolve` method - it will be called for any other integration.
Arguments for this method are up to you. Result is expected to have two parts - 
solution (string) and reason (preferably dict, but probably all json encodable objects will be alright).

For any configuration file there is `config` property available - supporting multiple config locations and parsing yaml files.



### Agent Exchange

Integration with Adjudication Engine / Agent Exchange with Data Source queries is implemented.

Data Source object will need to be defined, matching your request and response format.
Api protos can be found [here](https://gitlab.silenteight.com/sens/data-source-api/-/tree/master/data-source-api/src/main/proto/silenteight/datasource/api) - you can use existing data source or request/create a new implementation in used bridge.

Agent Exchange object doesn't need to be redefined, as it should work in default form for most common cases.
For smooth work in dev / prod environment, rabbitmq queues and user should be defined [probably here](https://gitlab.silenteight.com/devops/dev-nomad-cluster).
For UI integration your agent should appear in configuration [probably here](https://gitlab.silenteight.com/sens/serp-governance).


### Grpc Service

Grpc service is implemented, but Servicer Object matching your service protos will need to be defined.
Api protos can be found [here](https://gitlab.silenteight.com/ro/agents-api/-/tree/master/protocol-agents/src/main/proto/silenteight/agent)


### Deployment

Deployment is not currently included in this package.
Sample deployment scripts can be found in [company name agent](https://gitlab.silenteight.com/mcieslak/miscellaneous).


## Implementation

Services for agent are run with asyncio, to fully utilize resources in io heavy operations (rabbitmq messaging, grpc calls).
Resolving specific request will be computed in the same process, or, if `agent.processes` is defined in configuration file, will utilize multiprocessing pool.


## Tests

Tests are run with `pytest`. 

For gitlab runs, flag `--without-rabbitmq` is added - so tests with agent exchange
are not run, as it needs running rabbitmq instance. Those tests should be run locally after starting rabbitmq 
(for example using [this](https://gitlab.silenteight.com/sens/common-docker-infrastructure)).
If you have time - please make this tests run in gitlab.

### Code quality

* flake8
* black
* isort

All code quality checks are included in tox (see `tox.ini`) and are checked in both gitlab pipeline.
