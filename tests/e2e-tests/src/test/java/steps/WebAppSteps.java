package steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import utils.ScenarioContext;
import utils.datageneration.webapp.CreateUser;
import utils.datageneration.webapp.WebAppGenerationService;

import static io.restassured.RestAssured.given;
import static utils.AuthUtils.getAuthTokenHeaderForAdmin;

public class WebAppSteps implements En {
  ScenarioContext scenarioContext = Hooks.scenarioContext;

  public WebAppSteps() {
    And(
        "Users endpoint responses with status code 200",
        () -> given()
            .header("Authorization", getAuthTokenHeaderForAdmin())
            .when()
            .get("rest/webapp/api/users")
            .then()
            .statusCode(200));

    And(
        "Create user with random name",
        () -> {
          CreateUser user = WebAppGenerationService.createUser();

          scenarioContext.set(user.getUserName(), user);

          given()
              //TODO(dsniezek): fix dependency to allow jackson auto serialize to json
              .body(new ObjectMapper().writeValueAsString(user))
              .header("Authorization", getAuthTokenHeaderForAdmin())
              .contentType("application/json")
              .when()
              .post("/rest/webapp/api/users")
              .then()
              .statusCode(201);
        });
  }
}
