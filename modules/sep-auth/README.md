#SEP-Auth

This is a library to authenticate and authorize users within Keycloak.

## Authentication

### Keycloak

To use Keycloak authentication, you need to provide the property:
```
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: $KEYCLOAK_URI/realms/$REALM
```
Or
```
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: $KEYCLOAK_URI/realms/$REALM/protocol/openid-connect/certs
```
If the application should start even when Keycloak is not available

To fetch information from endpoints with the annotation @PreAuthorize("isAuthorized('PERMISSION_NAME')") you need to be login into the Keycloak.

### Block endpoints

Sometimes there is a need to block all endpoints (HSBC on a prod env).
To do this, you can run the application with a `no-rest-api` profile.
