# Karate Automated Tests

### How to run tests

1. **To run the tests using feature file, set the VM Options in run/debug config:**

`-Dkarate.env=bravo-dev -Dkarate.client-id=FILL_WITH_DATA -Dkarate.client-secret=FILL_WITH_DATA -Dkarate.username=FILL_WITH_DATA -Dkarate.password=FILL_WITH_DATA!`


2. **Run the tests using gradle command:**

`gradle test --tests ChooseRunnerName -Dkarate.env=bravo-dev -Dkarate.client-id=FILL_WITH_DATA -Dkarate.client-secret=FILL_WITH_DATA -Dkarate.username=FILL_WITH_DATA -Dkarate.password=FILL_WITH_DATA`

_example:_

`gradle test --tests SmokeTestRunner -Dkarate.env=bravo-dev -Dkarate.client-id=FILL_WITH_DATA -Dkarate.client-secret=FILL_WITH_DATA -Dkarate.username=FILL_WITH_DATA -Dkarate.password=FILL_WITH_DATA`
