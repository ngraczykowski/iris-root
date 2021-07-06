## Running tests in Gradle

By default, build run all tests. However, some integration tests take too much time.
In order to make work faster, we added the flag `-Punit` to run only unit tests. <br>

Example <br>
`./gradlew clean build ` - run build with all tests <br>
`./gradlew clean build -Punit` - run build only with unit tests

This same works with command `test` <br>
`./gradlew clean test ` - run all tests <br>
`./gradlew clean test -Punit` - run only unit tests

Tests are considered as integration when they have the word `Integration` in the class name 
eg: `AwsServiceIntegrationTest`. So it's important to name test classes appropriately. 
