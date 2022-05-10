package utils;

import static io.restassured.RestAssured.*;

public class AuthUtils {

  public String getAuthToken() {
    return given()
        .param("grant_type", "password")
        .param("username",System.getProperty("karate.username"))
        .param("password", System.getProperty("karate.password"))
        .param("client_id" ,System.getProperty("karate.client-id"))
        .param("client_secret", System.getProperty("karate.client-secret"))
        .post("https://auth.silenteight.com/realms/sens-webapp/protocol/openid-connect/token")
        .then()
        .statusCode(200)
        .extract()
        .response()
        .jsonPath()
        .getString("access_token");
  }
}
