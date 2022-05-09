package utils;

import com.intuit.karate.Http;

public class OAuthUtils {
  public String getAccessToken() {
    return Http.to("https://auth.silenteight.com/realms/sens-webapp/protocol/openid-connect/token")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .post(
            String.format(
                "grant_type=password&username=%s&password=%s&client_id=%s&client_secret=%s",
                System.getProperty("karate.username"),
                System.getProperty("karate.password"),
                System.getProperty("karate.client-id"),
                System.getProperty("karate.client-secret")))
        .json()
        .get("access_token");
  }
}
