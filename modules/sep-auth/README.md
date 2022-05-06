#SEP-Auth

This is a library to authenticate and authorize users within Keycloak.

## Authentication

### Keycloak

To use Keycloak authentication you need to provide the keycloak.adapter properties.

To fetch information from endpoints with the annotation @PreAuthorize("isAuthorized('PERMISSION_NAME')") you need to be login into the Keycloak.

### Basic

For development purpose, you can run your application with a `basic-auth` profile.

All endpoints will be then protected only by a basic authorization configured in a properties file. 
So you will be able to run the application without Keyclaok in your local environment.

An example properties file:
    
    # settings used in the development process to allow testing with swagger and without keycloak or any other
    # authorization server

    # simple auth settings
    sep.auth.basic.users:
        - username: bo
          password: bodev
          roles:
            - BUSINESS_OPERATOR

An example OpenAPI configuration, ie: `BasicAuthOpenApiConfiguration.java`:

    @OpenAPIDefinition(security = {
        @SecurityRequirement(name = BasicAuthOpenApiConfiguration.APP_SECURITY_SCHEMA)
    })
    @SecurityScheme(
        name = BasicAuthOpenApiConfiguration.APP_SECURITY_SCHEMA,
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        in = SecuritySchemeIn.DEFAULT
    )
    @Configuration
    @Profile("swagger & basic-auth")
    class BasicAuthOpenApiConfiguration {
    
        static final String APP_SECURITY_SCHEMA = "basic-security";
    }


Full example is available in this Marge Request:
https://gitlab.silenteight.com/sens/simulator/-/merge_requests/19
 
### Block endpoints

Sometimes there is a need to block all endpoints (HSBC on a prod env).
To do this, you can run the application with a `no-rest-api` profile.
All endpoint will be available with a `basic-auth`, but there will be no user available to log in.
