# ETL pipeline

Generic project that help Data Scientists in analyze of the Client's data as well as Developers in creation of fast and scalable architecture of microservices. 

How to use it and customize - read this notebook: `"notebooks/Local json pipeline.ipynb"`

ETL pipeline assumes that payload is of the following structure:

```
{
    "alertPayload.alert.headerInfo.uniqueCustomerId": "R_US_Active_Address_A05003324172_2020-01-07-07.06.28.836480",
    "alertPayload.alert.headerInfo.datasetId": "1044",
    "alertPayload.alert.headerInfo.datasetName": "R_US_Active_Address",
    "alertPayload.alert.headerInfo.masterId": "72951854",
    "alertPayload.alert.headerInfo.currentVersionId": "122438658",
    "alertPayload.alert.headerInfo.stopDescriptors.stopDescriptor.name": "Joe Doe",
    "alertPayload.alert.headerInfo.firstVersionCreatedDt": "2020-01-08T09:31:30.264-05:00",
    ...
    "alertPayload.supplementalInfo.relatedParties.party[0].fields.partyLastName": "Vutin",
    "alertPayload.supplementalInfo.relatedParties.party[0].fields.partyType": "Individual",
    "alertPayload.supplementalInfo.relatedParties.party[0].fields.countryOfIncorporation": null,
    "alertPayload.supplementalInfo.relatedParties.party[0].fields.dobDate": "02/31/1900",
    "alertPayload.supplementalInfo.relatedParties.party[0].fields.partyPrimaryCitizenshipCountry": "United States",
    "alertPayload.supplementalInfo.relatedParties.party[0].fields.partyCountryOfBirth": null,
    "alertPayload.supplementalInfo.relatedParties.party[0].fields.partyFirstName": "Pladimir",
}
```

Keys that begin with "alertPayload.alert" are derived from alert `xml` file. Opening tag is the `<alert></alert>`.
Keys that begin with "alertPayload.supplementalInfo" are derived from `json` file.

Before this flat payload is fed into ETL pipeline, the instance of `PayloadLoader` needs to have converted the flat payload to the json format with match ids:

```
{'alert': {'headerInfo': {'currentVersionId': '122438658',
   'datasetId': '1044',
   'datasetName': 'R_US_Active_Address',
   'firstVersionCreatedDt': '2020-01-08T09:31:30.264-05:00',
   'inputVersionSample': 'R_US_Active_Address_A05003324172_2020-01-07-07.06.',
   'lastVersionUpdatedDt': '2020-01-08T09:31:30.264-05:00',
   'masterId': '72951854',
   'masterVersion': '412740c151535f0756e8dbec7440b726c7a3e135',
   'stopDescriptors': {'stopDescriptor': {'name': 'Joe Doe'}},
   'uniqueCustomerId': 'R_US_Active_Address_A05003324172_2020-01-07-07.06.28.836480'},
  'inputRecordHist': [{'createdDate': '01/08/20',
    'field': [{'isScreenable': 'false',
      'name': 'SOURCE_REF',
      'sortOrder': '1',
      'value': 'R_US_Active_Address_A05003324172_2020-01-07-07.06.28.836480'},
     {'isScreenable': 'false',
      'name': 'UNIQUE_KEY',
      'sortOrder': '2',
      'value': 'A05003324172'},
     {'isScreenable': 'false', 'name': 'BARCODE', 'sortOrder': '3'},
     {'isScreenable': 'false',
      'name': 'ADDRESS_TYPE',
      'sortOrder': '4',
...
```


# Service



## Docker

1. Copy paste directory `config` and set up `config/service/service.yaml`

2. Pull docker image:

`docker pull docker.repo.silenteight.com/etl-pipeline-service`

3. Launch:

`docker run  -v <PATH_TO_CONFIG>:/config -p <PORT FROM YAML>:<PORT FROM YAML> -e CONFIG_APP_DIR=/config docker.repo.silenteight.com/etl-pipeline-service`



## Locally

1. Install etl service the newest version (it's >=0.5.5!)"

`pip install etl_pipeline`

2. Copy paste directory `config` and set up `config/service/service.yaml` settings

`CONFIG_APP_DIR=<PATH TO CONFIG DIRECTORY> python -m etl_pipeline`



## Emulating environment

1. You can launch service via:
`CONFIG_APP_DIR=etl_service/config python etl_pipeline`

2. You can launch data source service stub via:
`python tests/test_custom/data_source_stub.py`

3. You can now send request as shown in:
`pytest ./tests/test_custom/test_client.py`



## Config

### Service

You can configure:
- `DATA_SOURCE_INPUT_ENDPOINT`, e.g. `localhost:50052` - the endpoint of Data Source Service where ETL pipeline sends created agent inputs and;
- `ETL_SERVICE_PORT`, e.g. `9090`  - port at which the ETL pipeline service is available;



### Agents

You can configure for which agents the input is created from payloads. For example in `config/agents/agents_input_WM_ADDRESS.yaml`

```
residency_agent:
  ap:
  - alert.inputRecordHist.INPUT_FIELD.ADDRESS1_COUNTRY
  wl:
  - matchRecords.WL_COUNTRY
```

the pipeline takes field from payload - `payload["alert"]["inputRecordHist"]["INPUT_FIELD"]["ADDRESS1_COUNTRY"]` and add its as alerted party payload to Residency Agent input. 



### Pipeline

You can configure params like a fuzziness level for WL match tokens selection or field names.


# Playground

1. Load submodules: `git submodule update --init --recursive`
2. Docker login:
```
docker login docker.repo.silenteight.com
```
2. Pull image
```
docker pull docker.repo.silenteight.com/etl-pipeline
```

3. Launch:
```
docker-compose -f docker/docker-compose.yaml up
```
4. If a log "======= Notebook launched ========" occurs, please go to `localhost:4444` in your browser and explore Data Platform - MVP.ipynb. 

Required output:
```
Attaching to docker_jupyter_playground_1
jupyter_playground_1   | ======= Notebook launched ========
```

Output is saved to `data` directory in real time after each step. Offline results are also available in the `tests/data` directory. 