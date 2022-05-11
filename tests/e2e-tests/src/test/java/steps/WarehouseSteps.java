package steps;

import io.cucumber.java.en.And;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.junit.Assert;
import utils.FilesHelper;
import utils.Hooks;
import utils.ScenarioContext;
import utils.datageneration.Batch;
import utils.datageneration.DataGenerationService;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.util.concurrent.TimeUnit.SECONDS;

public class WarehouseSteps {

  DataGenerationService dataGenerationService = new DataGenerationService();
  FilesHelper filesHelper = new FilesHelper();
  ScenarioContext scenarioContext = Hooks.scenarioContext;

  @And("Initialize generation of {string} report via warehouse and wait until it's generated")
  public void generateReportViaWarehouse(String value) {
    Batch batch = (Batch) scenarioContext.get("batch");

    Response response = given()
        .urlEncodingEnabled(true)
        .param("from", batch.getGenerationStartTime())
        .param("to", dataGenerationService.getDateTimeNow())
        .post("rest/warehouse/api/v2/analysis/production/reports/" + value);

    response
        .then()
        .statusCode(200);

    scenarioContext.set("reportName", response.jsonPath().getString("reportName"));

    Awaitility.await().atMost(15, SECONDS).until(() -> when()
        .get("rest/warehouse/api/v2/" + scenarioContext.get("reportName") + "/status")
        .then()
        .extract()
        .response()
        .jsonPath()
        .getString("status")
        .equals("OK"));
  }

  @And("Download generated report")
  public void downloadReportFromWarehouse() {
    final InputStream report = given()
        .get("rest/warehouse/api/v2/" + scenarioContext.get("reportName"))
        .then()
        .statusCode(200)
        .extract()
        .asInputStream();

    scenarioContext.set("report", report);
  }

  @And("Downloaded report contains {int} rows")
  public void reportContainsXRows(int size) {
    Assert.assertEquals(
        size, filesHelper.getRowsNumber((InputStream) scenarioContext.get("report")));
  }
}
