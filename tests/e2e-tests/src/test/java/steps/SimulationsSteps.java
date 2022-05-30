package steps;

import io.cucumber.java8.En;
import io.restassured.http.ContentType;
import org.awaitility.Awaitility;
import utils.ScenarioContext;
import utils.datageneration.namescreening.Batch;
import utils.datageneration.CommonUtils;
import utils.datageneration.simulations.Dataset;
import utils.datageneration.simulations.Simulation;
import utils.datageneration.simulations.SimulationGenerationService;

import java.time.Duration;

import static io.restassured.RestAssured.*;
import static java.util.concurrent.TimeUnit.MINUTES;

public class SimulationsSteps implements En {

  SimulationGenerationService simulationGenerationService = new SimulationGenerationService();
  ScenarioContext scenarioContext = Hooks.scenarioContext;
  CommonUtils commonUtils = new CommonUtils();

  public SimulationsSteps() {
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

    And(
        "Create dataset with name {string} for recently created learning",
        (String value) -> {
          Batch learningBatch = (Batch) scenarioContext.get("learningBatch");

          Dataset dataset = simulationGenerationService.generateDataset(value,
              commonUtils.getOnlyDateWithOffset(0), commonUtils.getOnlyDateWithOffset(1));
          scenarioContext.set("dataset", dataset);

          given()
              .body(dataset.getCreationPayload())
              .contentType(ContentType.JSON)
              .when()
              .post("/rest/simulator/api/v1/datasets")
              .then().statusCode(201);
        });

    And(
        "Create simulation based on created policy and dataset with name {string}",
        (String value) -> {
          Simulation simulation = simulationGenerationService.generateSimulation(value);
          scenarioContext.set("simulation", simulation);

          given()
              .body(simulation.getCreationPayload())
              .contentType("application/json")
              .when()
              .post("/rest/simulator/api/v1/simulations")
              .then()
              .statusCode(201);
        });
    And(
        "Wait until simulation is done",
        () -> {
          Simulation simulation = (Simulation) scenarioContext.get("simulation");

              when()
              .get(String.format("/rest/simulator/api/v1/simulations/%s", simulation.getUuid()))
              .then()
              .statusCode(200);

          Awaitility
              .await()
              .atMost(3, MINUTES)
              .pollInterval(Duration.ofSeconds(5))
              .until(() -> when()
                  .get(String.format("/rest/simulator/api/v1/simulations/%s", simulation.getUuid()))
                  .then()
                  .extract()
                  .response()
                  .jsonPath()
                  .getString("state")
                  .equals("DONE"));
        });
  }
}
