package steps.api;

import io.cucumber.java8.En;
import org.awaitility.Awaitility;
import utils.datageneration.simulations.CreateDataset;
import utils.datageneration.simulations.CreateSimulation;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.util.concurrent.TimeUnit.MINUTES;
import static steps.Hooks.scenarioContext;
import static utils.CommonUtils.getOnlyDateWithOffset;
import static utils.datageneration.simulations.SimulationGenerationService.createDataset;
import static utils.datageneration.simulations.SimulationGenerationService.createSimulation;

public class SimulationsSteps implements En {

  public SimulationsSteps() {
    And(
        "Simulation endpoint responses with status code {int}",
        (Integer code) ->
            given()
                .param("state", "NEW", "PENDING", "RUNNING", "DONE", "ERROR")
                .when()
                .get("rest/simulator/api/v1/simulations")
                .then()
                .statusCode(code));

    And("Datasets endpoint responses with status code 200", () -> {
      when().get("rest/simulator/api/v1/datasets").then().statusCode(200);
    });

    And(
        "Create dataset with name {string} for recently created learning",
        (String value) -> {
          CreateDataset dataset =
              createDataset(value, getOnlyDateWithOffset(0), getOnlyDateWithOffset(1));
          scenarioContext.set("dataset", dataset);

          given()
              .body(dataset)
              .when()
              .post("/rest/simulator/api/v1/datasets")
              .then()
              .statusCode(201);
        });

    And(
        "Create simulation based on created policy and dataset with name {string}",
        (String value) -> {
          CreateSimulation simulation = createSimulation(value);
          scenarioContext.set("simulation", simulation);

          given()
              .body(simulation)
              .when()
              .post("/rest/simulator/api/v1/simulations")
              .then()
              .statusCode(201);
        });

    And(
        "Wait until simulation is done",
        () -> {
          CreateSimulation simulation = (CreateSimulation) scenarioContext.get("simulation");

          when()
              .get(String.format("/rest/simulator/api/v1/simulations/%s", simulation.getId()))
              .then()
              .statusCode(200);

          Awaitility.await()
              .atMost(3, MINUTES)
              .pollInterval(Duration.ofSeconds(5))
              .until(
                  () ->
                      when()
                          .get(
                              String.format(
                                  "/rest/simulator/api/v1/simulations/%s", simulation.getId()))
                          .then()
                          .extract()
                          .response()
                          .jsonPath()
                          .getString("state")
                          .equals("DONE"));
        });
  }
}
