# SEAR Payments Bridge

This project represents the high-level implementation of SEAR Payments using heavily Spring Integration as a platform. It assumes using Adjudication Engine (AE) as alert recommendation platform (not using our own agents and implementation here).  

## To run it

1. Clone Adjudication Engine project https://gitlab.silenteight.com/sens/adjudication-engine
2. Follow the AE readme to run it
3. Start the database using `make up`
4. In Run configuration settings change 'Use class path module' to 'sear-payments-bridge.sear-payments-app.main'
5. Enable the following Spring profiles for development: `logrequest,mockdatasource,mockaws`
6. Run the Spring Application using as a main class `PaymentsBridgeApplication`.

### Start tsaas-bridge cmapi-mockup

Entire flow assumes that alerts are sent back to Cmapi. Hence, I strongly suggest that you start `cmapi-mock` as explained here: https://gitlab.silenteight.com/scb/tsaas-bridge/-/tree/master/cmapi-mock.

However, I run it on a non-standard port, hence please use the following command to start it:

```
<PAYMENTS_BRIDGE_HOME>/cmapi-mock/scripts/start-web.sh --port=24609
```

This step is optional and you may try to test the flow without the outbound callback. However, you will see most likely lots of errors in your log file while sending back recommendation.

### Submit a request with an alert

Use my prepared script to submit a sample request:

```
./scripts/submit-request.sh
```

# Authenticate the request to the server

## Enabling endpoint security in development

The development configuration disables service security by default, to ease with testing.
But for testing the authorization itself, you need to enable it.

To enable security, set the following property:

    pb.security.disable-security: false

> **NOTE**
>
> The security is enabled by default.

## Getting access token from Keycloak

To get access token for the client `sierra-dev-api` run: 

    curl \
        -d client_id=sierra-dev-api \
        -d client_secret=$CLIENT_SECRET \
        -d grant_type=client_credentials \
        https://auth.silent8.cloud/realms/sierra/protocol/openid-connect/token

where `$CLIENT_SECRET` is available [here](https://auth.silent8.cloud/admin/master/console/#/realms/sierra/clients/1e5bb2aa-d17b-4746-8e24-fd3bb21d1259/credentials).

### Test access token

The generated access token `$TOKEN` can be tested by following the steps below:

1. Start the `PaymentsBridgeApplication`.
1. Place the `$TOKEN` in the command below and run it:

        curl 'http://localhost:24602/rest/payments/test-authentication' --header 'Authorization: Bearer $TOKEN'

1. Verify the output:
    
        Authenticated
        
# Authorize the requests to the CMAPI

## Configuration of CMAPI Auth Server

1. Set the following settings:
    - `security.oauth2.client.client-secret` (or the `SECURITY_OAUTH2_CLIENT_CLIENT_SECRET` environment variable).
1. Enable callback by setting:
    - `pb.cmapi.callback.enabled: true`

### Test CMAPI callback

1. Run `cmapi-mock` by following its [README.md](cmapi-mock/README.md).
1. Send a request to http://localhost:24602/rest/payments/test-callback Details on how to a send request are in [Test access token](#test-access-token) section.
1. Verify the `Headers: Authorization: Bearer` from `cmapi-mock` debug log.
