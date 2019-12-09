# Silent Eight Names Screening Web Application

## Development setup

### Prerequisites

|Prerequisite|Necessity|Remarks|
|:-----------:|:-------:|-------|
|**Java 8**  |Mandatory| CAS uses Java 8, you can  [download and install packages for your system from Azul Systems, Inc.](https://www.azul.com/downloads/zulu-community/?&version=java-8-lts); make sure you install full Java Development Kit (JDK). |
|**Java 11** |Mandatory| Web App API uses Java 11, you can  [download and install packages for your system from Azul Systems, Inc.](https://www.azul.com/downloads/zulu-community/?&version=java-11-lts); make sure you install full Java Development Kit (JDK). |
|**Docker**  |Mandatory| Running database for Web App API, which greatly simplifies development setup. |
|**IntelliJ**|Mandatory| Project is prepared for being worked on in IntelliJ IDEA. |
|**jEnv**    |Optional | jEnv helps with managing Java versions. [Download it from jEnv site](https://www.jenv.be/) and follow instructions for installing.|   
|**Gradle**  |Optional | SERP comes with gradle wrapper that you should use for building the project. Nevertheless you might want to have Gradle installed.|


### Git repositories

* [sens-webapp](https://gitlab.silenteight.com/sens/sens-webapp): main project responsible for:
  * [sens-webapp-frontend](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-frontend): Web App User Interface
  * [sens-webapp-backend](https://gitlab.silenteight.com/sens/sens-webapp/tree/master/sens-webapp-backend): Web App REST API 
* [scb-screening](https://gitlab.silenteight.com/scb/scb-screening): SENS application repository also that handle Auth server (CAS):
  * [cas-server](https://gitlab.silenteight.com/scb/scb-screening/tree/master/cas-server): [CAS project](https://apereo.github.io/cas/6.1.x/index.html) used to provide Single Sign-On mechanism for Web App users. 

### Run services

**CAS**

1. Pull `scb-screening` git repository.   
2. Set `Java 8`
3. Run script to start CAS server:

        ./scb-screening/cas-server/start.sh

    Successfully started CAS server displays below message on a console:
    
        <Started CasWebApplication in XX.YY seconds (JVM running for XX.YYY)>

**Web App API**

1. Run PostgreSQL database as a Docker container:

        docker-compose up

    Successfully started PostgreSQL instance displays below message on a console:

        database system is ready to accept connections
    
2. Run `WebApplication` class as a **Spring Boot** service directly from **IntelliJ IDEA**. 
   
   As an alternative you can start Web App API from Gradle script:
   
        ./gradlew sens-webapp-backend:bootRun
    
**Web App UI**

1. Run script to start Web App UI:

        ./sens-webapp-frontend/npm start

   Successfully started application displays below message on a console:
   
        ** Angular Live Development Server is listening on localhost:4200, open your browser on http://localhost:4200/ **

2. Open `http://localhost:4200/` URL in a browser.  

3. Browser redirects unauthenticated user to `http://localhost:7071/cas/login?service=http%3A%2F%2Flocalhost%3A7070%2Flogin%2Fcas` to perform authentication.

### Configuration

#### Database

Web App API is using PostgreSQL database to keep Web App specific data.  
Database configuration stored in a file: `sens-webapp-backend/resources/sens-webapp.yml`  

Example configuration:

    sens.webapp.db.host: localhost
    sens.webapp.db.port: 5432
    sens.webapp.db.schema: public
    sens.webapp.db.name: sens_webapp_db
    sens.webapp.db.user: sens_webapp
    sens.webapp.db.password: sens_webapp_pass
    sens.webapp.db.url: jdbc:postgresql://${sens.webapp.db.host}:${sens.webapp.db.port}/${sens.webapp.db.name}?currentSchema=${sens.webapp.db.schema}


