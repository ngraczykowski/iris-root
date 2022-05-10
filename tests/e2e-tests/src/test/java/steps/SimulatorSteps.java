package steps;

import io.cucumber.java.en.And;

import static io.restassured.RestAssured.*;

public class SimulatorSteps {
  
  @And("Simulation endpoint responses with status code 200")
  public void simulationEndpointIsAlive() {
    given()
        .param("state", "NEW", "PENDING", "RUNNING", "DONE", "ERROR")
        .when()
        .get("rest/simulator/api/v1/simulations")
        .then()
        .statusCode(200);
  }

  @And("Datasets endpoint responses with status code 200")
  public void datasetsEndpointIsAlive() {
        when()
        .get("rest/simulator/api/v1/datasets")
        .then()
        .statusCode(200);
  }
}
