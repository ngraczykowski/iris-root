package steps;

import io.cucumber.java.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import utils.AuthUtils;
import utils.ScenarioContext;

public class Hooks {

  public static ScenarioContext scenarioContext = new ScenarioContext();

  @BeforeAll
  public static void setupRestAssured() {
    RestAssured.baseURI = "https://bravo.dev.silenteight.com/";
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.requestSpecification = new RequestSpecBuilder()
        .build()
        .header("Authorization", "Bearer " + new AuthUtils().getAuthToken())
        .given()
        .log()
        .all();
  }
}
