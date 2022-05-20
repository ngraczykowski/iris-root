package steps;

import io.cucumber.java8.En;

import static io.restassured.RestAssured.*;

public class SimulatorSteps implements En {

  public SimulatorSteps() {
    And("Simulation endpoint responses with status code 200", () -> {
      given()
          .param("state", "NEW", "PENDING", "RUNNING", "DONE", "ERROR")
          .when()
          .get("rest/simulator/api/v1/simulations")
          .then()
          .statusCode(200);
    });
    And("Datasets endpoint responses with status code 200", () -> {
      when().get("rest/simulator/api/v1/datasets").then().statusCode(200);
    });
  }
}
