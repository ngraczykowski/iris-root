# Notes regarding GNS-RT performance testing

# Prerequisite

- SLA for GNS-RT is 30 TPS with 90 at the peak
- cutoff time for GNS-RT is 7 seconds

# Test locally

## Environment

- 16 cpus 11th Gen Intel(R) Core(TM) i7-11800H @ 2.30GHz
- 32 gb ram
- java18 with jvm opts `-Xmx1024m -Xms1024m`

## Steps

- run scb-bridge locally with profiles: `dev,client`, note: if you wish to use local oracle, include profile `local-oracle` as well and invoke `docker-compose --profile oracle up -d`
- with `dev` profile activated Mocks will be used instead of Uds and Core-Bridge
    - the Mocks will simulate latencies by sleeping some random time on endpoint calls
    - with those latencies each RT request should be processed in 3500 ms to 7000 ms, which does not
      include local calls to DB
- make sure following exchanges are created in RabbitMq - you will see error/warn in
  application logs that some exchange does not exist - go to RabbitMq and create one
  - `core-bridge.recommendation-delivered-exchange`
  - `core-bridge.notify-batch-error-exchange`
  - `core-bridge.notify-batch-completed-exchange`
- issue from cmd:

```bash
ab -p req.json  -T application/json -c 500 -n 1600 -v 1 localhost:24220/rest/scb-bridge/v1/gnsrt/recommendation
 ```

- make sure all responses(at least P95) are below 8 seconds
- there are no error responses

- then increase C ten times and verify sustained TPS reached 90 TPS with no error responses and no
  big change in time distributions.

e.x (only parts of output):

```
Concurrency Level:      500
Time taken for tests:   178.365 seconds
Complete requests:      16000
Failed requests:        0
Requests per second:    89.70 [#/sec] (mean)
Time per request:       5573.905 [ms] (mean)

Percentage of the requests served within a certain time (ms)
  50%   5271
  66%   5767
  75%   6035
  80%   6189
  90%   6487
  95%   6644
  98%   6772
  99%   6964
 100%   9115 (longest request)
```

# Observations

- no webflux is used but instead servlet 3 async feature
    - when put log level to DEBUG it is clear it uses DispatcherServlet and ASYNC processing
    - GnsRt Controller is started from TASK thread, which is a thread that handles blocking threads
      in undertow
- tested on tomcat and undertow returns very similar result
- when used webflux:
    - response times were very similar, but if we switch the app to use webflux we loose all old filters and other
      interceptors which were prepared for Servlet env
    - GnsRt Controller is started from IO thread, which is a thread that handles IO as expected
    
When replace reactive code with blocking:
   - response times went up 2-3 times

EOF