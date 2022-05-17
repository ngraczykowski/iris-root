# IRIS Automated Tests

### How to run tests

1. **To run the tests using feature file, set the VM Options in run/debug config:**

`-Dtest.clientId=${clientId}
-Dtest.username=${username}
-Dtest.password=${password}
-Dtest.url=${baseUrl}
-Dtest.isRegression=false`


2. **Run the tests using gradle command:**

`gradle test
-Dcucumber.features=src/test/java/features/SmokeTests.feature
-Dcucumber.plugin=html:report.html
-Dtest.isRegression=false
-Dtest.clientId=${clientId}
-Dtest.username=${username}
-Dtest.password=${password}
-Dtest.url=${baseUrl}`
