package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.junit.Assert;
import utils.ScenarioContext;
import utils.datageneration.namescreening.Batch;
import utils.datageneration.warehouse.CreateCountryGroup;
import utils.datageneration.warehouse.WarehouseGenerationService;

import java.time.Duration;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.util.concurrent.TimeUnit.SECONDS;

import static utils.AuthUtils.getAuthTokenHeaderForAdmin;
import static utils.datageneration.CommonUtils.getDateTimeNow;

public class WarehouseSteps implements En {

  static final String COUNTRY_GROUP = "countryGroup";
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
    And("Create country group", this::createCountryGroup);
    And("Add countries to country group", this::addCountriesToCountryGroup);
  }

  private void createCountryGroup() throws JsonProcessingException {
    CreateCountryGroup countryGroup =
        WarehouseGenerationService.createCountryGroup(UUID.randomUUID());

    scenarioContext.set(COUNTRY_GROUP, countryGroup);

    given()
        // TODO(dsniezek): fix dependency to allow jackson auto serialize to json
        .body(new ObjectMapper().writeValueAsString(countryGroup))
        .header("Authorization", getAuthTokenHeaderForAdmin())
        .contentType("application/json")
        .when()
        .post("rest/warehouse/api/v1/countryGroups")
        .then()
        .statusCode(201);
  }

  private void addCountriesToCountryGroup(DataTable countries) throws JsonProcessingException {
    CreateCountryGroup countryGroup = (CreateCountryGroup)scenarioContext.get(COUNTRY_GROUP);
    given()
        // TODO(dsniezek): fix dependency to allow jackson auto serialize to json
        .body(new ObjectMapper().writeValueAsString(countries.asList()))
        .header("Authorization", getAuthTokenHeaderForAdmin())
        .contentType("application/json")
        .when()
        .put(String.format("api/v1/countryGroups/%s/countries", countryGroup.getId()))
        .then()
        .statusCode(200);
  }

  private void generateReport(String reportName) {
    Batch batch = (Batch) scenarioContext.get("batch");

    Response response =
        given()
            .urlEncodingEnabled(true)
            .param("from", batch.getGenerationStartTime())
            .param("to", getDateTimeNow())
            .post("rest/warehouse/api/v2/analysis/production/reports/" + reportName);

    response.then().statusCode(200);

    scenarioContext.set("reportName", response.jsonPath().getString("reportName"));

    Awaitility.await()
        .atMost(15, SECONDS)
        .pollInterval(Duration.ofSeconds(1))
        .until(
            () ->
                when()
                    .get("rest/warehouse/api/v2/" + scenarioContext.get("reportName") + "/status")
                    .then()
                    .extract()
                    .response()
                    .jsonPath()
                    .getString("status")
                    .equals("OK"));
  }

  private void downloadReport() {
    final String report =
        given()
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
