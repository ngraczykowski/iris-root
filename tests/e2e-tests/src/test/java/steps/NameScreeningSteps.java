package steps;

import io.cucumber.java8.En;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import utils.ScenarioContext;
import utils.datageneration.namescreening.Batch;
import utils.datageneration.namescreening.BatchGenerationService;

import java.time.Duration;

import static io.restassured.RestAssured.*;
import static java.util.concurrent.TimeUnit.SECONDS;

public class NameScreeningSteps implements En {

  BatchGenerationService batchGenerationService = new BatchGenerationService();
  ScenarioContext scenarioContext = Hooks.scenarioContext;

  public NameScreeningSteps() {
    And(
        "Send a batch with {int} alerts on solving and wait until it's solved",
        (Integer size) -> {
          Batch batch = batchGenerationService.generateBatchWithSize(size, "solve");
          Batch learningBatch = batchGenerationService.generateBatchFrom(batch, "learning");
          scenarioContext.set("batch", batch);
          scenarioContext.set("learningBatch", learningBatch);

          given()
              .body(batch.getPayload())
              .contentType(ContentType.JSON)
              .when()
              .post("rest/hsbc-bridge/async/batch/v1/" + batch.getId() + "/recommend")
              .then()
              .statusCode(200);

          Awaitility.await()
              .atMost(15, SECONDS)
              .pollInterval(Duration.ofSeconds(1))
              .until(
                  () ->
                      "COMPLETED"
                          .equals(
                              when()
                                  .get(
                                      "rest/hsbc-bridge/async/batch/v1/"
                                          + batch.getId()
                                          + "/status")
                                  .then()
                                  .extract()
                                  .response()
                                  .jsonPath()
                                  .getString("batchStatus")));
        });

    And(
        "Get result for batch and send on ingest",
        () -> {
          Batch batch = (Batch) scenarioContext.get("batch");

          Response response =
              given()
                  .body(batch.getPayload())
                  .when()
                  .get("rest/hsbc-bridge/async/batch/v1/" + batch.getId() + "/result");

          response.then().statusCode(200);

          given()
              .contentType(ContentType.JSON)
              .body(response.getBody().asString())
              .when()
              .post("rest/hsbc-bridge/async/batch/v1/ingestRecommendations")
              .then()
              .statusCode(200);
        });

    And(
        "Send batch on learning",
        () -> {
          Batch batch = (Batch) scenarioContext.get("learningBatch");

          given()
              .body(batch.getPayload())
              .when()
              .contentType(ContentType.JSON)
              .post("rest/hsbc-bridge/async/batch/v1/" + batch.getId() + "-learning/learning")
              .then()
              .statusCode(200);

          Awaitility.await()
              .atMost(15, SECONDS)
              .pollInterval(Duration.ofSeconds(1))
              .until(
                  () ->
                      "COMPLETED"
                          .equals(
                              when()
                                  .get(
                                      "rest/hsbc-bridge/async/batch/v1/"
                                          + batch.getId()
                                          + "-learning/status")
                                  .then()
                                  .extract()
                                  .response()
                                  .jsonPath()
                                  .getString("batchStatus")));
        });

    And(
        "Send batch with {int} alerts on learning",
        (Integer size) -> {
          Batch learningBatch = batchGenerationService.generateBatchWithSize(size, "learning");
          scenarioContext.set("learningBatch", learningBatch);

          given()
              .body(learningBatch.getPayload())
              .contentType(ContentType.JSON)
              .when()
              .post(
                  "rest/hsbc-bridge/async/batch/v1/" + learningBatch.getId() + "-learning/learning")
              .then()
              .statusCode(200);

          Awaitility.await()
              .atMost(15, SECONDS)
              .pollInterval(Duration.ofSeconds(1))
              .until(
                  () ->
                      "COMPLETED"
                          .equals(
                              when()
                                  .get(
                                      "rest/hsbc-bridge/async/batch/v1/"
                                          + learningBatch.getId()
                                          + "-learning/status")
                                  .then()
                                  .extract()
                                  .response()
                                  .jsonPath()
                                  .getString("batchStatus")));
        });
  }
}
