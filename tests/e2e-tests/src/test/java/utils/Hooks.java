package utils;

import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;

public class Hooks {

  public static ScenarioContext scenarioContext = new ScenarioContext();

  @BeforeAll
  public static void setupRestAssured() {
    RestAssured.baseURI = "https://bravo.dev.silenteight.com/";
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.requestSpecification = new RequestSpecBuilder()
        .build()
        .header("Authorization", "Bearer " + new AuthUtils().getAuthToken());
  }
}
