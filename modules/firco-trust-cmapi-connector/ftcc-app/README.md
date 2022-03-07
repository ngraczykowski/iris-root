# Module [APP]() 

## Build

In root repo dir execute:

```shell
$ ./gradlew clean build
```

## Run

Executable fat jar is in here:
**`sear-customers-bridge-app/build/libs/sear-customers-bridge-app-1.7.0-SNAPSHOT-exec.jar`**

```shell
$ java -jar -Dserver.port=8080 -Dpb.cmapi.callback.login=TCMAPI -Dpb.cmapi.callback.password="SECURE_PASS;)" -Dpb.request_file.directory=/tmp/custommer-bridge -Dpb.cmapi.callback.endpoint=http://localhost:8080/rest/pb/callback-recommendation sear-customers-bridge-app/build/libs/ftcc-app-VERSION_NUMBER-exec.jar
```

#### Property:

- `-Dserver.port=8080` - server PORT
- `-Dpb.request_file.directory=/tmp/custommer-bridge` - where to save request body File
- `-Dpb.cmapi.callback.endpoint=http://localhost:8080/rest/pb/callback-recommendation` - callback
  endpoint (in this case dummy callback handler)
- `-Dpb.cmapi.callback.login=TCMAPI` - callback trustLogin (default `TCMAPI`)
- `-Dpb.cmapi.callback.password="SECURE_PASS;)"` - callback trustPassword (
  default `r+ZhajxNEhWTfS9YV/lRwgbpDFgl`)
- `-Ddebug` - enable debug mode - print additional data

## [Examples](../http-tests/ftcc-tests.http) with test requests. 
