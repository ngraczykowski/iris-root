package steps;

import io.cucumber.java.en.And;

import static io.restassured.RestAssured.when;

public class GovernanceSteps {
  @And("Policies endpoint responses with status code 200")
  public void datasetsEndpointIsAlive() {
    when()
        .get("rest/governance/api/v1/policies")
        .then()
        .statusCode(200);
  }
}
