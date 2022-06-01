package utils;

import static io.restassured.RestAssured.*;

public class AuthUtils {
  private static final String OIDC_TOKEN_ENDPOINT_TEMPLATE = "%s/realms/%s/protocol/openid-connect/token";

  public String getAuthToken() {
    String realm = System.getProperty("test.realm");
    String authServerUrl = System.getProperty("test.authServerUrl");
    return given()
        .param("grant_type", "password")
        .param("username", System.getProperty("test.username"))
        .param("password", System.getProperty("test.password"))
        .param("client_id", System.getProperty("test.clientId"))
        .post(String.format(OIDC_TOKEN_ENDPOINT_TEMPLATE, authServerUrl, realm))
        .then()
        .statusCode(200)
        .extract()
        .response()
        .jsonPath()
        .getString("access_token");
  }

  public String getAuthTokenForAnotherUser() {
    String realm = System.getProperty("test.realm");
    String authServerUrl = System.getProperty("test.authServerUrl");
    return given()
        .param("grant_type", "password")
        .param("username", System.getProperty("test.username2"))
        .param("password", System.getProperty("test.password2"))
        .param("client_id", System.getProperty("test.clientId"))
        .post(String.format(OIDC_TOKEN_ENDPOINT_TEMPLATE, authServerUrl, realm))
        .then()
        .statusCode(200)
        .extract()
        .response()
        .jsonPath()
        .getString("access_token");
  }
}
