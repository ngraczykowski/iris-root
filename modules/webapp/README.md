# Silent Eight Names Screening Web Application

[[_TOC_]]

## Git repositories

Below is a list of repositories relevant for WebApp:
* [sens-webapp](https://gitlab.silenteight.com/sens/sens-webapp): Main WebApp repository
* [serp](https://gitlab.silenteight.com/sens/serp): Silent Eight Realtime Platform - an alert resolving engine
* [sep-auth](https://gitlab.silenteight.com/sep/sep-auth): Authorization Module
* [sep-usermanagement-api](https://gitlab.silenteight.com/sep/sep-usermanagement-api): Interface for user management
* [sep-usermanagement-keycloak](https://gitlab.silenteight.com/sep/sep-usermanagement-keycloak): Keycloak implementation of an user management interface
* [sep-front](https://gitlab.silenteight.com/sens/sep-front): Frontend for WebApp
* [keycloak-import](https://gitlab.silenteight.com/sens/keycloak-imports): Keycloak configuration

## Development setup

### Prerequisites

|Prerequisite|Necessity|Remarks|
|:-----------:|:-------:|-------|
|**Java 11** |Mandatory| Web App API uses Java 11, you can  [download and install packages for your system from Azul Systems, Inc.](https://www.azul.com/downloads/zulu-community/?&version=java-11-lts); make sure you install full Java Development Kit (JDK). |
|**Docker**  |Mandatory| Running database for Web App API in a container. |
|**Docker-Compose**  |Mandatory| Docker compose orchestrator. |
|**IntelliJ**|Mandatory| Project is prepared for being worked on in IntelliJ IDEA. |
|**jEnv**    |Optional | jEnv helps with managing Java versions. [Download it from jEnv site](https://www.jenv.be/) and follow instructions for installing.|   
|**Gradle**  |Optional | SERP comes with gradle wrapper that you should use for building the project. Nevertheless you might want to have Gradle installed.|
|**jq**      |Optional | jq is JSON manipulation utility that is being used during Keycloak configuration export. Is is widely used, therefore should be in every popular linux distro repositories. |

Access to password manager - [Bitwarden](https://bitwarden.silenteight.com/)

Properly configured environment - see [Technical Guide](https://docs.google.com/document/d/1-bL5fzQtJfaO3LswByoyI539rnCXr9gDSkb56iTqtmo)

### Initial setup

WebApp depends on infrastructure and services managed by SERP CLI, therefore before you start working
with WebApp you need to setup SERP first.
 
In order to do that checkout [SERP](https://gitlab.silenteight.com/sens/serp) repository and 
follow [README.md](https://gitlab.silenteight.com/sens/serp/-/blob/master/README.md).

Make sure you have completed at least the following:

1. Set system-wide environment variable `SERP_HOME` that points at a SERP directory, e.g.:
   ```
   sudo bash -c "echo 'SERP_HOME=/path/to/serp' >> /etc/environment"
   ```    
   Make sure this change takes effect (restart may be needed):
   ```
   echo "${SERP_HOME}"
   ```
   
1. Install `serp-opt-installer` on top of your existing SERP directory, e.g.:
   ```
   cd "${SERP_HOME}" && cd ..
   wget "https://silenteight.com/repo/jenkins/serp-opt-installer/master/34-IdabJv/serp-opt-installer-1.25.0.run"
   chmod +x serp-opt-installer-1.25.0.run
   ./serp-opt-installer-1.25.0.run
   ```

1. Build SERP
   ```
   gw build
   ```

1. Start the SERP postgres db via docker-compose
   ```
   cd "${SERP_HOME}"
   docker-compose up -d postgres
   ```
   
1. Configure db settings, password placeholders can be filled in based on values stored in Bitwarden:
   ```
   ./bin/serp config set-many \
   db.host="localhost" \
   db.port="24290" \
   db.schema="public" \
   db.name="serp" \
   db.user="serp" \
   db.password="serp" \
   gns.db.host="oracle.silenteight.com" \
   gns.db.port="1521" \
   gns.db.service.name="ORCLCDB" \
   gns.db.user="LC_GNS_WEB_SIT_01" \
   gns.db.password=<password>
   ```
   
1. Generate certificates:
   ```
   ./bin/serp cert generate
   ```

1. Add the following RootCA certificate as trusted in your web browser:
   ```
   ./cert/root-ca/root-ca.pem
   ```

1. Start SERP 
   ```
   ./bin/serp start
   ```    

1. Import decision tree
   ```
   ./bin/serp dt import conf/dt/dt-serp-deny-migrated.json 
   ```

1. Create test user
   ```
   ./bin/serp user create -u model_tuner -d "Model Tuner" -r "Model Tuner" --password "Password1!"
   ``` 
   You may want to use `scripts/create-users.sh` to create multiple users with various rules all at once.

1. Stop SERP
   ```
   ./bin/serp stop
   ```   

   
### Starting services

#### SERP
1. Start the supervisor:
   ```
   ./bin/serp ctl start-supervisor
   ``` 
   
1. Start the infrastructure components: rabbitmq, traefik, keycloak, caddy
   ```
   ./bin/serp ctl start infra:* 
   ```
   
1. Start the following services: governance, circuit-breaker:
   ```
   ./bin/serp ctl start app:governance
   ./bin/serp ctl start app:circuit-breaker
   ``` 
   
#### Keycloak Import Scripts
   
Keycloak import scripts updates clients definition using:
```
 ./bin/serp keycloak import
```

SERP modules are using different clients and it may occur that after manual import   
tokens sessions has been invalidated and already issued tokens become also invalidated on the server side. 
This information is not propagated to the clients. As a result any attempt to authenticate using these tokens will result in 401 error. 
During next attempt Keycloak client will request new tokens and the services will continue to work.

Example:
    
```
./bin/serp report accounts
ERROR: 500 Server Error: Internal Server Error for url: https://localhost:24111/rest/webapp/api/reports/accounts-report
```
In web-app log you may also see:

```
 javax.ws.rs.NotAuthorizedException: HTTP 401 Unauthorized
```   

#### WebApp
Start the WebApp using "WebApplication" run configuration in IntelliJ.

### Using services
By default, you can reach UI for components at:
- WebApp: https://localhost:24111/
- Keycloak admin: https://localhost:24111/auth/admin
- Consul: https://localhost:24120/ui/dc1/services (requires mTLS)

### Using Swagger UI
By default, swagger is disabled in production. To enable it you need to apply `swagger` Spring Boot profile.
UI: [http://localhost:24410/rest/webapp/openapi/swagger-ui/index.html?configUrl=/rest/webapp/openapi/api-docs/swagger-config](http://localhost:24410/rest/webapp/openapi/swagger-ui/index.html?configUrl=/rest/webapp/openapi/api-docs/swagger-config).

In order to make authenticated requests 
click the key lock icon, type in desired client id (normally this would be `frontend`) 
and click the `Authorize` button. 

### Database
By default, SERP is configured to use a single database for all components, but it is possible
to configure separate db for WebApp:

1. Start the webapp postgres db via docker-compose
   ```
   cd sens-webapp
   docker-compose up -d
   ```
   
1. Configure SERP    
   ```
   ./bin/serp config set-many \
   webapp.db.host="localhost" \
   webapp.db.port="24480" \
   webapp.db.schema="public" \
   webapp.db.name="sens" \
   webapp.db.user="sens" \
   webapp.db.password="sens"
   ```

1. It is sufficient to restart WebApp only.

### Users
The production scripts imported at Keycloak startup create a single admin account that can be
used to manage Keycloak's admin panel. In order to access the application you need to create
a separate application-specific account. 

You can use `scripts/create-users.sh` script to pre-create the following users:

| Username         | User role          | Password            
|------------------|--------------------|---------------------
|model_tuner       | Model Tuner        | Password1! 
|approver          | Approver           | Password1! 
|auditor           | Auditor            | Password1!
|user_administrator| User Administrator | Password1!
|QA                | QA                 | Password1!
|QA Issue Manager  | QA Issue Manager   | Password1!
|superuser         | < all above >      | Password1!
 
This script requires the following services to be running:  Webapp, Keycloak

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

## Local running without SERP 
You don't need to have SERP installed to run WebApp on your local environment. 

In order to run WebApp locally without SERP: 

1. Start the webapp postgres db via docker-compose
   ```
   cd sens-webapp
   docker-compose up -d
   ```

2. Run WebApp in IntelliJ with WebApplication - local configuration, or alternatively run gradle `bootRun` task with specified args (check `WebApplication - local` configuration).

WebApp needs currently keycloak to be ready to use, so you need to have keycloak installed e.g. via docker
You need to specify your keycloak configuration in application.yml:
```
    keycloak:
        client-id: <your-client-id>
        adapter:
            auth-server-url: https://auth.silenteight.com
            realm: sens-webapp
            public-client: false
            confidential-port: 0
            principal-attribute: preferred_username
            ssl-required: external
```