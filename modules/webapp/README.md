# Silent Eight Names Screening Web Application

## Development setup

### Prerequisites

|Prerequisite|Necessity|Remarks|
|:-----------:|:-------:|-------|
|**Java 11** |Mandatory| Web App API uses Java 11, you can  [download and install packages for your system from Azul Systems, Inc.](https://www.azul.com/downloads/zulu-community/?&version=java-11-lts); make sure you install full Java Development Kit (JDK). |
|**Docker**  |Mandatory| Running database and keycloak server for Web App API, which greatly simplifies development setup. |
|**IntelliJ**|Mandatory| Project is prepared for being worked on in IntelliJ IDEA. |
|**jEnv**    |Optional | jEnv helps with managing Java versions. [Download it from jEnv site](https://www.jenv.be/) and follow instructions for installing.|   
|**Gradle**  |Optional | SERP comes with gradle wrapper that you should use for building the project. Nevertheless you might want to have Gradle installed.|


### Git repositories

* [sens-webapp](https://gitlab.silenteight.com/sens/sens-webapp): main project responsible for:
  * [sens-webapp-frontend](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-frontend): Web App User Interface
  * [sens-webapp-backend](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-backend): Web App REST API 
* [scb-screening](https://gitlab.silenteight.com/scb/scb-screening): SENS application repository
* [serp](https://gitlab.silenteight.com/sens/serp): Silent Eight Realtime Platform responsible for recommendations generation for given alerts

### Run services

**Keycloak auth server and main database**

1. Definitions for both containers can be found in `docker-compose.yml`. Run Keycloak and PostgreSQL with:
        
        docker-compose up

**SERP**

1. Pull `serp` git repository.

2. Follow installation instructions from [README](https://gitlab.silenteight.com/sens/serp/blob/master/README.md) file.

**Web App API**
    
1. Run `WebApplication` class as a **Spring Boot** service directly from **IntelliJ IDEA**. 

In Spring Boot Run Configuration add following program arguments:

        --serp.home=<path_to_root_of_serp_project>
   
   As an alternative you can start Web App API from Gradle script:
   
        ./gradlew sens-webapp-backend:bootRun --args='--serp.home=<path_to_root_of_serp_project>'
    
**Web App UI**

1. Run script to start Web App UI:

        ./sens-webapp-frontend/npm start

   Successfully started application displays below message on a console:
   
        ** Angular Live Development Server is listening on localhost:4200, open your browser on http://localhost:4200/ **

2. Open `http://localhost:4200/` URL in a browser.  

### Configuration

#### Keycloak

Whole development keycloak configuration should be stored in file: `/conf/keycloak/sens-webapp-realm.json`.
Everything can be configured using GUI available at [localhost:8081](http://localhost:8081).
To export created configuration use script: `./keycloak-scripts/export-realm.sh`. (WARNING: After 'boot-up' is completed, you need to kill the script by hand with CTRL-C).

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

### Continuous Integration

Jenkins job: [sens/sens-webapp](https://jenkins.silenteight.com/job/sens/job/sens%252Fsens-webapp/)  
Project in Sonar: [Silent Eight Name Screening Web Application](https://sonar.silenteight.com/dashboard?id=com.silenteight.sens.webapp%3Awebapp)

Based on `Jenkinsfile` added to the project source code:
* Jenkins automatically [creates jobs](https://jenkins.silenteight.com/view/Current/job/sens/job/sens%252Fsens-webapp/) for each branch and runs pipeline for each change in the branch.  
[GitLab Branch Source Plugin](https://jenkins.io/blog/2019/08/23/introducing-gitlab-branch-source-plugin/) in Jenkins scans projects and creates Jenkins jobs automatically.   
In addition, git hooks in project have been configured to run Jenkins job.
* MergeBot automatically merges MRs that have at least 2 thumb-up approvals and 'ready for merge' label.   
* Jenkins executes pipeline that:
  * Checkouts source code,
  * Runs tests,
  * Verifies code base changes against SonarQube Quality Gate.
