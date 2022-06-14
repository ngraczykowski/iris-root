package steps.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.common.mapper.TypeRef;
import utils.datageneration.warehouse.CreateCountryGroup;
import utils.datageneration.webapp.User;
import utils.datageneration.webapp.WebAppGenerationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static steps.Hooks.scenarioContext;
import static steps.api.WarehouseSteps.COUNTRY_GROUP;
import static utils.AuthUtils.getAuthTokenHeaderForAdmin;

public class WebAppSteps implements En {

  public WebAppSteps() {
    And(
        "Users endpoint responses with status code 200",
        () ->
            given()
                .auth()
                .oauth2(getAuthTokenHeaderForAdmin())
                .when()
                .get("rest/webapp/api/users")
                .then()
                .statusCode(200));

    And(
        "Create user {string} with random name",
        (String label) -> {
          var token = getAuthTokenHeaderForAdmin();
          User user = WebAppGenerationService.createUser(label);
          given()
              .body(user)
              .auth()
              .oauth2(token)
              .when()
              .post("/rest/webapp/api/users")
              .then()
              .statusCode(201);
          scenarioContext.setUser(label, user);
        });
    And("Assign user {string} to country group", this::assignUserToCountryGroup);
    And("Delete user {string}", this::deleteUser);
    And("Assign user {string} to roles", this::assignUserToRoles);
  }

  private void assignUserToRoles(String label, DataTable roles) throws JsonProcessingException {
    // ["AUDITOR","MODEL_TUNER","APPROVER","USER_ADMINISTRATOR"]
    patchUser(label, Map.of("roles", roles.asList()));
  }

  private void assignUserToCountryGroup(String label) throws JsonProcessingException {
    // "countryGroups":["warehouse_business_user_dev"]
    CreateCountryGroup countryGroup = (CreateCountryGroup) scenarioContext.get(COUNTRY_GROUP);
    patchUser(label, Map.of("countryGroups", new UUID[] {countryGroup.getId()}));
  }

  private void patchUser(String label, Map<String, Object> patch) throws JsonProcessingException {
    User user = scenarioContext.getUser(label);

    var token = getAuthTokenHeaderForAdmin();

    var userData =
        given()
            .auth()
            .oauth2(token)
            .get("/rest/webapp/api/users")
            .then()
            .extract()
            .response()
            .as(new TypeRef<List<Map<String, Object>>>() {})
            .stream()
            .filter(x -> user.getUserName().equals(x.get("userName")))
            .findAny()
            .orElseThrow();

    var body = new HashMap<>(userData);
    body.putAll(patch);
    body.remove("createdAt");
    body.remove("lastLoginAt");
    body.remove("lockedAt");
    body.remove("origin");
    body.remove("userName");

    given()
        .body(body)
        .auth()
        .oauth2(token)
        .when()
        .patch(String.format("/rest/webapp/api/users/%s", user.getUserName()))
        .then()
        .statusCode(204);
    scenarioContext.setUserToken(user.getUserName(), null);
  }

  private void deleteUser(String label) {
    var user = scenarioContext.getUser(label);
    given()
        .header("Authorization", getAuthTokenHeaderForAdmin())
        .contentType("application/json")
        .when()
        .delete(String.format("/rest/webapp/api/users/%s", user.getUserName()))
        .then()
        .statusCode(204);
    scenarioContext.setUserToken(user.getUserName(), null);
  }
}
