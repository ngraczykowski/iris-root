# Silent Eight Names Screening Web Application

[[_TOC_]]

## Development setup

### Prerequisites

|Prerequisite|Necessity|Remarks|
|:-----------:|:-------:|-------|
|**Java 11** |Mandatory| Web App API uses Java 11, you can  [download and install packages for your system from Azul Systems, Inc.](https://www.azul.com/downloads/zulu-community/?&version=java-11-lts); make sure you install full Java Development Kit (JDK). |
|**Docker**  |Mandatory| Running database and keycloak server for Web App API, which greatly simplifies development setup. |
|**IntelliJ**|Mandatory| Project is prepared for being worked on in IntelliJ IDEA. |
|**jEnv**    |Optional | jEnv helps with managing Java versions. [Download it from jEnv site](https://www.jenv.be/) and follow instructions for installing.|   
|**Gradle**  |Optional | SERP comes with gradle wrapper that you should use for building the project. Nevertheless you might want to have Gradle installed.|
|**jq**      |Optional | jq is JSON manipulation utility that is being used during Keycloak configuration export. Is is widely used, therefore should be in every popular linux distro repositories. |


### Git repositories

* [sens-webapp](https://gitlab.silenteight.com/sens/sens-webapp): main project responsible for:
  * [sens-webapp-frontend](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-frontend): Web App User Interface
  * [sens-webapp-backend](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-backend): Web App REST API 
* [scb-screening](https://gitlab.silenteight.com/scb/scb-screening): SENS application repository
* [serp](https://gitlab.silenteight.com/sens/serp): Silent Eight Realtime Platform responsible for recommendations generation for given alerts

### Run services

#### Keycloak auth server, external SAML and main database

1. Definitions for all containers can be found in [docker-compose.yml](docker-compose.yml). Run Keycloak and PostgreSQL with:
        
        docker-compose up

2. Log into [Keycloak admin console](http://localhost:8081/auth/admin/) using `sens:sens` credentials, 
go to `Import` and import users file located in [conf/keycloak dir](conf/keycloak), with `Overwrite` setting.

#### SERP

1. Pull `serp` git repository.

2. Follow installation instructions from [README](https://gitlab.silenteight.com/sens/serp/blob/master/README.md) file.

#### Web App UI

1. Run script to start Web App UI:

        ./sens-webapp-frontend/npm start

   Successfully started application displays below message on a console:
   
        ** Angular Live Development Server is listening on localhost:4200, open your browser on http://localhost:4200/ **

2. Open `http://localhost:4200/` URL in a browser.  

#### Web App API

Prior to running API make sure you ran [UI](#web-app-ui), as it starts reverse proxy required by whole app to run correctly.
   
| Profile | Behaviour                                                                                                                                                                                                                                                                                                                                       |
|---------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `dev`   | Uses app properties stored in [`application-dev.yml`](sens-webapp-backend/src/main/resources/application-dev.yml). At startup, loads raw Keycloak config stored in [`conf/keycloak/sens-webapp-realm.json`](conf/keycloak/sens-webapp-realm.json). Additionally launches Swagger UI at [/openapi/ endpoint](localhost:7070/rest/webapp/openapi).|
| `prod`  | Uses app properties stored in [`application-prod.yml`](sens-webapp-backend/src/main/resources/application-prod.yml). At startup, loads Keycloak production template stored in [`Keycloak module resources`](sens-webapp-keycloak/src/main/resources/configuration-templates) and fills it with values passed in properties.                     |
    
1. Run `WebApplication` class as a **Spring Boot** service directly from **IntelliJ IDEA**. 

In Spring Boot Run Configuration add following program arguments:

        --serp.home=<path_to_root_of_serp_project>
   
   As an alternative you can start Web App API from Gradle script:
   
        ./gradlew sens-webapp-backend:bootRun --args='--serp.home=<path_to_root_of_serp_project>'
    

### Configuration

#### Keycloak
Whole Keycloak development configuration is stored in [conf/keycloak](conf/keycloak) (or [conf/keycloak-saml-idp](conf/keycloak-saml-idp) for SAML idP).
Everything can be configured using GUI available at:
- [localhost:8081](http://localhost:8081) for main Keycloak instance
- [localhost:8095](http://localhost:8095) for SAML idP Keycloak instance

##### Users and roles
Currently we support following roles:
- Analyst
- Auditor
- Business Operator
- Admin (composes of Analyst, Auditor, Business Operator)

Users for development purposes are as follows:

| username            | password            | roles             |
|---------------------|---------------------|-------------------|
| `admin`             | `admin`             | Admin             |
| `analyst`           | `analyst`           | Analyst           |
| `auditor`           | `auditor`           | Auditor           |
| `business_operator` | `business_operator` | Business Operator |

##### Exporting
To export created configuration use either: 
- [keycloak-scripts/export-config-for-main-keycloak.sh](keycloak-scripts/export-config-for-main-keycloak.sh).
- [keycloak-scripts/export-config-for-saml-keycloak.sh](keycloak-scripts/export-config-for-saml-keycloak.sh).
>>>
WARNING: Exporting requires booting up another Keycloak instance in desired container.
Afterwards such instance is being automatically killed, but on lower spec machines it might take longer.
Therefore, if the config is not exported within default 20 seconds, you can set `KEYCLOAK_EXPORT_TIMEOUT` 
environment variable or modify default timeout in [this](keycloak-scripts/internal/1-export-realm.sh) file.
>>>
##### Reloading new configuration
`docker-compose down -v --remove-orphans && docker-compose up -d`

#### Database

Web App API is using PostgreSQL database to persist Web App specific data.  
Database connection configuration is stored in file: `sens-webapp-backend/resources/sens-webapp.yml`.  
It operates on database started by docker-compose command, but can be customized if needed.

Example configuration:

    sens.webapp.db.host: localhost
    sens.webapp.db.port: 5432
    sens.webapp.db.schema: public
    sens.webapp.db.name: sens_webapp_db
    sens.webapp.db.user: sens_webapp
    sens.webapp.db.password: sens_webapp_pass
    sens.webapp.db.url: jdbc:postgresql://${sens.webapp.db.host}:${sens.webapp.db.port}/${sens.webapp.db.name}?currentSchema=${sens.webapp.db.schema}

## Continuous Integration

Jenkins job: [sens/sens-webapp](https://jenkins.silenteight.com/job/sens/job/sens%252Fsens-webapp/)  
Project in Sonar: [Silent Eight Name Screening Web Application](https://sonar.silenteight.com/dashboard?id=com.silenteight.sens.webapp%3Awebapp)

Based on [Jenkinsfile](Jenkinsfile) added to the project source code:
* Jenkins automatically [creates jobs](https://jenkins.silenteight.com/view/Current/job/sens/job/sens%252Fsens-webapp/) for each branch and runs pipeline for each change in the branch.  
[GitLab Branch Source Plugin](https://jenkins.io/blog/2019/08/23/introducing-gitlab-branch-source-plugin/) in Jenkins scans projects and creates Jenkins jobs automatically.   
In addition, git hooks in project have been configured to run Jenkins job.
* MergeBot automatically merges MRs that have at least 2 thumb-up approvals and 'ready for merge' label.   
* Jenkins executes pipeline that:
  * Checkouts source code,
  * Runs tests,
  * Verifies code base changes against SonarQube Quality Gate.

## Web App Release

Web App is a dependency of the SERP product.  
It means that Web App is packed into `jar` file and included in SERP project to run Web App API
next to other SERP services.

### Release procedure

1. Make sure that all [required commits are in `master` branch](https://gitlab.silenteight.com/sens/sens-webapp/commits/master),
1. Open [Web App project Jenkins job](https://jenkins.silenteight.com/job/sens/job/sens%252Fsens-webapp/job/master/),
1. Use `Build with Parameters` action to build Web App release `jar`.   
Select `release` option to release a build and publish `jars` into a Artifactory.
1. After successful build you can check `jar` files [directly in Artifactory](https://repo.silenteight.com/webapp/#/artifacts/browse/tree/General/libs-release-local/com/silenteight/sens/webapp).  
Note: Please login to Artifactory to see the newly published files.
1. Adjust Web App version in SERP project.  
Change value of the `silenteightSensWebappVersion` property in `gradle/scripts/dependencies.gradle` 
Gradle script to the released Web App version. 
1. Create MR with your changes. Wait for an approval and merge it.
1. [SERP release can be prepared now](https://jenkins.silenteight.com/job/sens/job/sens%252Fserp/).  
New snapshot/release version will start using newly released Web App release.

