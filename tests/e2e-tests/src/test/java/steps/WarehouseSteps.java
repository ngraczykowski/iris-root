package steps;

import io.cucumber.java8.En;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.junit.Assert;
import utils.FilesHelper;
import utils.ScenarioContext;
import utils.datageneration.namescreening.Batch;
import utils.datageneration.CommonUtils;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.util.concurrent.TimeUnit.SECONDS;

public class WarehouseSteps implements En {

  CommonUtils commonUtils = new CommonUtils();
  FilesHelper filesHelper = new FilesHelper();
  ScenarioContext scenarioContext = Hooks.scenarioContext;

  public WarehouseSteps() {
    And("Initialize generation of {string} HSBC report via warehouse and wait until it's generated",
        this::generateReport);
    And("Download generated HSBC report", this::downloadReport);
    And("Downloaded HSBC report contains {int} rows", this::assertRowsNumber);

    And("Initialize generation of {string} PB report via warehouse and wait until it's generated",
        this::generateReport);
    And("Download generated PB report", this::downloadReport);
    And("Downloaded PB report contains {int} rows", this::assertRowsNumber);
  }

  private void generateReport(String reportName) {
    Batch batch = (Batch) scenarioContext.get("batch");

    Response response = given()
        .urlEncodingEnabled(true)
        .param("from", batch.getGenerationStartTime())
        .param("to", commonUtils.getDateTimeNow())
        .post("rest/warehouse/api/v2/analysis/production/reports/" + reportName);

    response.then().statusCode(200);

    scenarioContext.set("reportName", response.jsonPath().getString("reportName"));

    Awaitility
        .await()
        .atMost(15, SECONDS)
        .pollInterval(Duration.ofSeconds(1))
        .until(() -> when()
            .get("rest/warehouse/api/v2/" + scenarioContext.get("reportName") + "/status")
            .then()
            .extract()
            .response()
            .jsonPath()
            .getString("status")
            .equals("OK"));
  }

  private void downloadReport() {
    final String report = given()
        .get("rest/warehouse/api/v2/" + scenarioContext.get("reportName"))
        .then()
        .statusCode(200)
        .extract()
        .asString();

    scenarioContext.set("report", report);
  }

  private void assertRowsNumber(int rowsNum) {
    String report = (String) scenarioContext.get("report");
    Assert.assertEquals(rowsNum, report.lines().count());
  }
}
