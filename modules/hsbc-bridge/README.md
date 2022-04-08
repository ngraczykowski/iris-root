# Testing

## Efficiency test

There is a script `effciency_test.sh` in package `.hsbc-bridge.scripts` to automate efficiency
testing. The script sends requests on the `/recommend` bridge endpoint in a given period of time.
The body is read from a file. Another request is sent when the previous one is marked as`COMPLETED`.
`COMPLETED` status is checked on endpoint `/status` in the loop with a given pause between requests.

The script logs hour of a sent request and when a given batch was 'COMPLETED'.
</br>E.g.</br>
`08:19:00 Sending request for batchId: test-08:19:00`</br>
`{"batchId":"test-08:19:00"}`</br>
`08:19:14 Batch with batchId: test-08:19:00 is "COMPLETED"`</br>

A result is the number of processed batches in a given time. E.g. `5 batches completed in 1 minute`.

#### Settings

Below you can find all settings you can change in the script.

| name                               | description                                                     | default value                   |
|------------------------------------|-----------------------------------------------------------------|---------------------------------|
| runtime                            | How long test should send requests.                             | 3 minutes                       |
| pause_between_check_status_request | Time in seconds between requests to check is batch 'COMPLETED'. | 10                              |
| path_to_json                       | Path to file with a prepared JSON payload.                      | ./efficiency_test.json          |
| request_url                        | URL for requests dependable on the environment.                 | localhost:24802/async/batch/v1/ |

## Running tests in Gradle

By default, build run all tests. However, some integration tests take too much time. In order to
make work faster, we added the flag `-Punit` to run only unit tests. <br>

Example <br>
`./gradlew clean build ` - run build with all tests <br>
`./gradlew clean build -Punit` - run build only with unit tests

This same works with command `test` <br>
`./gradlew clean test ` - run all tests <br>
`./gradlew clean test -Punit` - run only unit tests

Tests are considered as integration when they have the word `Integration` in the class name
eg: `AwsServiceIntegrationTest`. So it's important to name test classes appropriately.
