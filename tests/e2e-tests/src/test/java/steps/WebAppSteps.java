package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import utils.ScenarioContext;
import utils.datageneration.warehouse.CreateCountryGroup;
import utils.datageneration.webapp.CreateUser;
import utils.datageneration.webapp.WebAppGenerationService;

import java.util.HashMap;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static steps.WarehouseSteps.COUNTRY_GROUP;
import static utils.AuthUtils.getAuthTokenHeaderForAdmin;

public class WebAppSteps implements En {

  private static final String USER = "user";
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

          scenarioContext.set(USER, user);

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
    And("Assign user to country group", this::assignUserToCountryGroup);
  }

  private void assignUserToCountryGroup() throws JsonProcessingException {
    CreateUser createUser = (CreateUser)scenarioContext.get(USER);
    CreateCountryGroup countryGroup = (CreateCountryGroup)scenarioContext.get(COUNTRY_GROUP);
    HashMap<String, Object> body = new HashMap<>();
    body.put("countryGroups", new UUID[]{ countryGroup.getId()});
    given()
        //TODO(dsniezek): fix dependency to allow jackson auto serialize to json
        .body(new ObjectMapper().writeValueAsString(body))
        .header("Authorization", getAuthTokenHeaderForAdmin())
        .contentType("application/json")
        .when()
        .patch(String.format("/rest/webapp/api/users/%s", createUser.getUserName()))
        .then()
        .statusCode(204);
  }
}
