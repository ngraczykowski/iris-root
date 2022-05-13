package steps;

import io.cucumber.java.en.And;
import io.restassured.response.Response;
import utils.datageneration.DataGenerationService;
import utils.datageneration.Policy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class GovernanceSteps {

  DataGenerationService dataGenerationService = new DataGenerationService();

  @And("Policies endpoint responses with status code 200")
  public void datasetsEndpointIsAlive() {
    when()
        .get("rest/governance/api/v1/policies")
        .then()
        .statusCode(200);
  }

  @And("Prepare policy steps and features")
  public void preparePolicyStepsAndFeatures(List<Map<String, String>> stepsAndFeatures) {

  }

  @And("Create new policy with name {string}")
  public void createPolicyWithName(String value) {
    Policy policy =
        dataGenerationService.generatePolicyWithSteps(value, "DRAFT", Collections.emptyList());

    Response response = given()
        .body(policy.getCreationPayload())
        .contentType("application/json")
        .when()
        .post("/rest/governance/api/v1/policies");

    response
        .then()
        .statusCode(202);
  }

  @And("Add predefined steps with features")
  public void addPredefinedStepsToPolicy() {

  }
}
