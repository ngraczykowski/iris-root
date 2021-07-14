# sear-payments-spring-integration-mockup

This project represents the high-level implementation of SEAR Payments using heavily Spring Integration as a platform. It assumes using Adjudication Engine (AE) as alert recommendation platform (not using our own agents and implementation here).  

## To run it

1. Clone Adjudication Engine project https://gitlab.silenteight.com/sens/adjudication-engine
2. Follow AE readme to run it
3. Start db using `docker-compose -d`  
4. Run the Spring Application using as a main class `SearPaymentsApplication`.

### Start tsaas-bridge cmapi-mockup

Entire flow assumes that alerts are sent back to Cmapi. Hence, I strongly suggest that you start `cmapi-mock` as explained here: https://gitlab.silenteight.com/scb/tsaas-bridge/-/tree/master/cmapi-mock.

However, I run it on a non-standard port, hence please use the following command to start it:

```
<TSAAS_BRIDGE_HOME>/cmapi-mock/scripts/start-web.sh --port=24609
```

This step is optional and you may try to test the flow without the outbound callback. However, you will see most likely lots of errors in your log file while sending back recommendation.

### Submit a request with an alert

Use my prepared script to submit a sample request:

```
./scripts/submit-request.sh
```
