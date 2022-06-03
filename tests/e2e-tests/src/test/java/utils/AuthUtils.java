package utils;

import lombok.experimental.UtilityClass;

import static io.restassured.RestAssured.*;

// TODO(dsniezek): auth token can be reused
@UtilityClass
public class AuthUtils {

  private static final String REALM = System.getProperty("test.realm");

  private static final String AUTH_SERVER_URL = System.getProperty("test.authServerUrl");

  private static final String OIDC_TOKEN_ENDPOINT =
      String.format("%s/realms/%s/protocol/openid-connect/token", AUTH_SERVER_URL, REALM);

  public static String getAuthTokenHeaderForAdmin() {
    return getAuthTokenHeader(
        System.getProperty("test.username2"), System.getProperty("test.password2"));
  }

  public static String getAuthTokenHeader() {
    return getAuthTokenHeader(
        System.getProperty("test.username"), System.getProperty("test.password"));
  }

  private static String getAuthTokenHeader(String username, String password) {
    return "Bearer "
        + given()
            .param("grant_type", "password")
            .param("username", username)
            .param("password", password)
            .param("client_id", System.getProperty("test.clientId"))
            .post(OIDC_TOKEN_ENDPOINT)
            .then()
            .statusCode(200)
            .extract()
            .response()
            .jsonPath()
            .getString("access_token");
  }
}
