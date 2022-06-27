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

  private static final String SIM_API_BASE_PATH =
      System.getProperty("test.simApiBasePath", "rest/simulator/api/v1");

  public SimulationsSteps() {
    And(
        "Simulation endpoint responses with status code {int}",
        (Integer code) ->
            given()
                .param("state", "NEW", "PENDING", "RUNNING", "DONE", "ERROR")
                .when()
                .get(SIM_API_BASE_PATH + "/simulations")
                .then()
                .statusCode(code));

    And(
        "Datasets endpoint responses with status code 200",
        () -> {
          when().get(SIM_API_BASE_PATH + "/datasets").then().statusCode(200);
        });

    And(
        "Create dataset with name {string} for recently created learning",
        (String value) -> {
          CreateDataset dataset =
              createDataset(value, getOnlyDateWithOffset(0), getOnlyDateWithOffset(1));
          scenarioContext.set("dataset", dataset);

          given().body(dataset).when().post(SIM_API_BASE_PATH + "/datasets").then().statusCode(201);
        });

    And(
        "Create simulation based on created policy and dataset with name {string}",
        (String value) -> {
          CreateSimulation simulation = createSimulation(value);
          scenarioContext.set("simulation", simulation);

          given()
              .body(simulation)
              .when()
              .post(SIM_API_BASE_PATH + "/simulations")
              .then()
              .statusCode(201);
        });

    And(
        "Wait until simulation is done",
        () -> {
          CreateSimulation simulation = (CreateSimulation) scenarioContext.get("simulation");

          when()
              .get(SIM_API_BASE_PATH + String.format("/simulations/%s", simulation.getId()))
              .then()
              .statusCode(200);

          Awaitility.await()
              .atMost(3, MINUTES)
              .pollInterval(Duration.ofSeconds(5))
              .until(
                  () ->
                      when()
                          .get(
                              SIM_API_BASE_PATH
                                  + String.format("/simulations/%s", simulation.getId()))
                          .then()
                          .extract()
                          .response()
                          .jsonPath()
                          .getString("state")
                          .equals("DONE"));
        });
  }
}
