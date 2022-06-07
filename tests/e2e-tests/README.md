# IRIS Automated Tests

### How to run tests

1. **To run the tests using feature file, set the VM Options in run/debug config:**
```
-Dtest.clientId=${clientId}
-Dtest.username=${username}
-Dtest.password=${password}
-Dtest.admin.username=${username2}
-Dtest.admin.password=${password2}
-Dtest.realm=${realm}
-Dtest.authServerUrl=${authServerUrl}
-Dtest.url=${baseUrl}
```

2. **Run the tests using gradle command:**

```
gradle test \
-Dcucumber.features=src/test/java/features/SmokeTests.feature \
-Dcucumber.plugin=html:report.html \
-Dtest.isRegression=false \
-Dtest.clientId=${clientId} \
-Dtest.username=${username} \
-Dtest.password=${password} \
-Dtest.admin.username=${username2} \
-Dtest.admin.password=${password2} \
-Dtest.url=${baseUrl} \
-Dtest.realm=${realm} \
-Dtest.authServerUrl=${authServerUrl}
```
It is possible to specify a group of tests using one or more tags:
* @smoke
* @sierra
* @hotel
* @foxtrot
```
-Dcucumber.filter.tags="@smoke na @sierra"
```

3. **Test Parameters**
   clientId - Keycloak oAuth2 client ID (Defult: frontend)
   username - Keycloak username (Default: qa-test)
   password - Keycloak user password (Check in your Keycloak instance)
   url - IRIS base URL (Check in helm installation console output)
   realm - Keycloak realm (Default: Dev)
   authServerUrl - Keycloak instance URL (Check in helm installation console output)
