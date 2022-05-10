package steps;

import io.cucumber.java.en.And;

import io.restassured.response.Response;
import org.awaitility.Awaitility;
import utils.Hooks;
import utils.ScenarioContext;
import utils.datageneration.Batch;
import utils.datageneration.DataGenerationService;

import static io.restassured.RestAssured.*;
import static java.util.concurrent.TimeUnit.SECONDS;

public class BatchSteps {

  DataGenerationService dataGenerationService = new DataGenerationService();
  ScenarioContext scenarioContext = Hooks.scenarioContext;


  @And("Send a batch with {int} alerts on solving and wait until it's solved")
  public void sendBatchAndWaitUntilSolved(int size) {
    Batch batch = dataGenerationService.generateBatchWithSize(size);
    scenarioContext.set("batch", batch);

    given()
        .body(batch.getPayload())
        .when()
        .post("rest/hsbc-bridge/async/batch/v1/" + batch.getId() + "/recommend")
        .then()
        .statusCode(200);

    Awaitility.await().atMost(15, SECONDS).until(() -> when()
        .get("rest/hsbc-bridge/async/batch/v1/" + batch.getId() + "/status")
        .then()
        .extract()
        .response()
        .jsonPath()
        .getString("batchStatus")
        .equals("COMPLETED"));
  }

  @And("Get result for batch and send on ingest")
  public void getResultAndSendOnIngest() {
    Batch batch = (Batch) scenarioContext.get("batch");

    Response response =
        given()
            .body(batch.getPayload())
            .when()
            .get("rest/hsbc-bridge/async/batch/v1/" + batch.getId() + "/result");

    response.then().statusCode(200);

    given()
        .body(response)
        .when()
        .post("rest/hsbc-bridge/async/batch/v1/ingestRecommendations")
        .then()
        .statusCode(200);
  }

  @And("Send batch on learning")
  public void sendBatchOnLearning() {
    Batch batch = (Batch) scenarioContext.get("batch");

    given()
        .body(batch.getPayload())
        .when()
        .post("rest/hsbc-bridge/async/batch/v1/" + batch.getId() + "-learning/learning")
        .then()
        .statusCode(200);
  }
}
