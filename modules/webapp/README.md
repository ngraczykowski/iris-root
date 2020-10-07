# Silent Eight Names Screening Web Application

[[_TOC_]]

## Development setup

### Prerequisites

|Prerequisite|Necessity|Remarks|
|:-----------:|:-------:|-------|
|**Java 11** |Mandatory| Web App API uses Java 11, you can  [download and install packages for your system from Azul Systems, Inc.](https://www.azul.com/downloads/zulu-community/?&version=java-11-lts); make sure you install full Java Development Kit (JDK). |
|**Docker**  |Mandatory| Running database for Web App API, which greatly simplifies development setup. |
|**IntelliJ**|Mandatory| Project is prepared for being worked on in IntelliJ IDEA. |
|**jEnv**    |Optional | jEnv helps with managing Java versions. [Download it from jEnv site](https://www.jenv.be/) and follow instructions for installing.|   
|**Gradle**  |Optional | SERP comes with gradle wrapper that you should use for building the project. Nevertheless you might want to have Gradle installed.|


### Git repositories

* [sens-webapp](https://gitlab.silenteight.com/sens/sens-webapp): main project consisting of:
  * [sens-webapp-audit](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-audit): Web App Module generating Audit Report
  * [sens-webapp-backend](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-backend): Web App REST API
  * [sens-webapp-common](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-common): Web App Module with common tools
  * [sens-webapp-common-testing](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-common-testing): Web App Module with common tools for tests
  * [sens-webapp-db-changelog](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-db-changelog): Web App Module managing database schema
  * [sens-webapp-documentation](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-documentation): Web App Module containing Documentation
  * [sens-webapp-frontend](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-frontend): Web App User Interface
  * [sens-webapp-logging](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-logging): Web App Logging Module
  * [sens-webapp-report](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-report): Web App Module generating basic Reports
  * [sens-webapp-scb-chrome-extension](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-scb-chrome-extension): Web App Module for Chrome Extension integration
  * [sens-webapp-scb-report](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-scb-report): Web App Module generating SCB specific Reports
  * [sens-webapp-scb-user-sync](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-scb-user-sync): Web App Module for synchronizing SCB Analysts
  * [sens-webapp-user](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-user): Web App Module managing User Management
* [serp](https://gitlab.silenteight.com/sens/serp): Silent Eight Realtime Platform responsible for recommendations generation for given alerts

### Run services

#### WebApp database

1. Definitions for all containers can be found in [docker-compose.yml](docker-compose.yml). Run PostgreSQL with:
        
        docker-compose up -d

#### SERP

1. Pull `serp` git repository.

2. Follow installation instructions from [README](https://gitlab.silenteight.com/sens/serp/blob/master/README.md) file.

#### Web App UI

1. Run script to start Web App UI:

        ./sens-webapp-frontend/npm start

   Successfully started application displays below message on a console:
   
        ** Angular Live Development Server is listening on localhost:24400, open your browser on http://localhost:24400/ **

2. Open `http://localhost:24400/` URL in a browser.

#### Web App API

Prior to running API make sure you ran [UI](#web-app-ui), as it starts reverse proxy required by whole app to run correctly.
Also make sure that the project was built (`gw build`) before starting the API and the plugins are present in the ./plugin/webapp folder as they are used by the Web Application.
   
| Profile    | Behaviour                                                                                                                                                                                                                                                                                            |
|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `dev`      | Uses app properties stored in [`application-dev.yml`](sens-webapp-backend/src/main/resources/application-dev.yml). Additionally launches Swagger UI at [/openapi/ endpoint](localhost:24410/rest/webapp/openapi). |
| `prod`     | Uses app properties stored in [`application-prod.yml`](sens-webapp-backend/src/main/resources/application-prod.yml).                                   |
| `swagger`  | It enables Swagger UI for testing purposes                                                                                                                                                                                                                                                           |
  
1. Run `WebApplication` class as a **Spring Boot** service directly from **IntelliJ IDEA**. 

In Spring Boot Run Configuration add following program arguments:

        --serp.home=<path_to_root_of_serp_project>
   
   As an alternative you can start Web App API from Gradle script:
   
        ./gradlew sens-webapp-backend:bootRun --args='--serp.home=<path_to_root_of_serp_project>'
    

### Configuration

#### Database

Web App API is using PostgreSQL database to persist Web App specific data.  
Database connection configuration is stored in file: `sens-webapp-backend/resources/application.yml`.  
It operates on database started by docker-compose command, but can be customized if needed.

Example configuration:

    sens.webapp.db.host: localhost
    sens.webapp.db.port: 24480
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

## Using Swagger UI
To use Swagger UI you need apply `swagger` profile to your app, along with a main profile, e.g. --spring.profiles.active=dev,swagger
and go to [http://localhost:24410/rest/webapp/openapi/swagger-ui/index.html?configUrl=/rest/webapp/openapi/api-docs/swagger-config](http://localhost:24410/rest/webapp/openapi/swagger-ui/index.html?configUrl=/rest/webapp/openapi/api-docs/swagger-config).

In order to make authenticated requests 
click the key lock icon, type in desired client id (normally this would be `frontend`) 
and click the `Authorize` button. 
