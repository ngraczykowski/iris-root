package utils;

import lombok.experimental.UtilityClass;

import io.restassured.http.ContentType;
import utils.datageneration.webapp.User;

import static io.restassured.RestAssured.given;
import static steps.Hooks.BASE_URL;
import static steps.Hooks.scenarioContext;

// TODO(dsniezek): auth token can be reused
@UtilityClass
public class AuthUtils {

  private static final String REALM = System.getProperty("test.realm", "Dev");

  private static final String AUTH_SERVER_URL =
      System.getProperty("test.authServerUrl", BASE_URL + "/auth");

  private static final String OIDC_TOKEN_ENDPOINT =
      String.format("%s/realms/%s/protocol/openid-connect/token", AUTH_SERVER_URL, REALM);

  public static String getAuthTokenHeaderForAdmin() {
    var admin = scenarioContext.getAdminUser();
    if (admin == null) {
      throw new RuntimeException("admin user not set");
    }
    return getAuthTokenHeader(admin.getUserName(), admin.getPassword());
  }

  public static String getAuthTokenHeaderForDefaultUser() {
    var defaultUser = scenarioContext.getDefaultUser();
    if (defaultUser == null) {
      throw new RuntimeException("default user not set");
    }
    return getAuthTokenHeader(defaultUser.getUserName(), defaultUser.getPassword());
  }

  public static String getAuthTokenHeaderForUser(User user) {
    if (user == null) {
      throw new RuntimeException("custom user not set");
    }
    return getAuthTokenHeader(user.getUserName(), user.getPassword());
  }

  private static String getAuthTokenHeader(String username, String password) {
    return given()
        .auth()
        .none()
        .param("grant_type", "password")
        .param("username", username)
        .param("password", password)
        .param("client_id", System.getProperty("test.clientId", "frontend"))
        .contentType(ContentType.URLENC)
        .post(OIDC_TOKEN_ENDPOINT)
        .then()
        .statusCode(200)
        .extract()
        .response()
        .jsonPath()
        .getString("access_token");
  }
}
