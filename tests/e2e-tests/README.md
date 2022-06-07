# IRIS Automated Tests

### How to run tests

1. **To run the tests using feature file, set the VM Options in run/debug config:**

```
-Dtest.clientId=${clientId}
-Dtest.admin.username=${admin_username}
-Dtest.admin.password=${admin_password}
-Dtest.realm=${realm}
-Dtest.authServerUrl=${authServerUrl}
-Dtest.url=${baseUrl}
```

defaults:

* test.clientId=frontend
* test.realm=Dev
* test.authServerUrl=${test.url}/auth

So with the defaults the only required parameter is "test.url"

2. **Run the tests using gradle command:**

```
gradle test \
-Dcucumber.features="src/test/java/features[/a_feature_file.feature]" \
-Dcucumber.plugin=html:report.html \
-Dtest.clientId=${clientId} \
-Dtest.admin.username=${admin_username} \
-Dtest.admin.password=${admin_password} \
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
