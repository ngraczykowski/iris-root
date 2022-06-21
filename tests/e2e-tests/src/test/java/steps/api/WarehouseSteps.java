package steps.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.junit.Assert;
import utils.datageneration.namescreening.Batch;
import utils.datageneration.warehouse.CreateCountryGroup;
import utils.datageneration.warehouse.WarehouseGenerationService;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.util.concurrent.TimeUnit.SECONDS;
import static steps.Hooks.scenarioContext;
import static utils.AuthUtils.getAuthTokenHeaderForAdmin;
import static utils.CommonUtils.getDateTimeNow;
import static utils.CommonUtils.getDateTimeNowMinus;
import static utils.CommonUtils.getOnlyDateWithOffset;

public class WarehouseSteps implements En {

  static final String COUNTRY_GROUP = "countryGroup";

  public WarehouseSteps() {
    And("Initialize generation of {string} report via warehouse and wait until it's generated",
        this::generateReport);
    And("Download generated report", this::downloadReport);
    And("Downloaded report contains {int} rows", this::assertRowsNumber);
    And("Create country group", this::createCountryGroup);
    And("Add countries to country group", this::addCountriesToCountryGroup);
    And("Prepare report date range for today", this::prepareReportDateRangeForToday);
    And("Prepare report date range based on the last batch",
        this::prepareReportDateRangeForLastBatch);
    And("All entries have {string} equal to {string}", this::assertAllReportEntriesWithValue);
  }

  private void createCountryGroup() throws JsonProcessingException {
    CreateCountryGroup countryGroup =
        WarehouseGenerationService.createCountryGroup(UUID.randomUUID());

    scenarioContext.set(COUNTRY_GROUP, countryGroup);

    given()
        .body(countryGroup)
        .auth()
        .oauth2(getAuthTokenHeaderForAdmin())
        .when()
        .post("rest/warehouse/api/v1/countryGroups")
        .then()
        .statusCode(201);
  }

  private void addCountriesToCountryGroup(DataTable countries) throws JsonProcessingException {
    CreateCountryGroup countryGroup = (CreateCountryGroup) scenarioContext.get(COUNTRY_GROUP);
    given()
        .body(countries.asList())
        .auth()
        .oauth2(getAuthTokenHeaderForAdmin())
        .when()
        .put(
            String.format("rest/warehouse/api/v1/countryGroups/%s/countries", countryGroup.getId()))
        .then()
        .statusCode(200);
  }

  private void generateReport(String reportName) {
    Batch batch = (Batch) scenarioContext.get("batch");

    Response response =
        given()
            .urlEncodingEnabled(true)
            .contentType(ContentType.URLENC)
            .param(
                "from",
                Optional.ofNullable(batch)
                    .map(Batch::getGenerationStartTime)
                    .orElseGet(() -> getDateTimeNowMinus(Duration.ofMinutes(1))))
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

  private void downloadReport() throws IOException {
    final String report =
        given()
            .get("rest/warehouse/api/v2/" + scenarioContext.get("reportName"))
            .then()
            .statusCode(200)
            .extract()
            .asString();

    var schema =
        CsvSchema.emptySchema()
            .withHeader()
            .withLineSeparator("\n")
            .withColumnSeparator(',')
            .withQuoteChar('"');

    var parsedReport =
        new CsvMapper()
            .readerFor(Map.class)
            .with(schema)
            .<Map<String, String>>readValues(report)
            .readAll();

    scenarioContext.set("report", report);
    scenarioContext.set("parsedReport", parsedReport);
  }

  private void assertRowsNumber(int rowsNum) {
    var report = (List) scenarioContext.get("parsedReport");
    Assert.assertEquals(rowsNum, report.size());
  }

  private void assertAllReportEntriesWithValue(String field, String value) {
    var report = (List<Map<String, String>>) scenarioContext.get("parsedReport");
    report.forEach(entry -> Assert.assertEquals("entry " + entry, value, entry.get(field)));
  }

  private void prepareReportDateRangeForToday() {
    scenarioContext.set("from", getOnlyDateWithOffset(0));
    scenarioContext.set("to",getOnlyDateWithOffset(1));
  }

  private void prepareReportDateRangeForLastBatch() {
    Batch batch = (Batch) scenarioContext.get("batch");
    scenarioContext.set("from",batch.getGenerationStartTime());
    scenarioContext.set("to",getDateTimeNow());
  }
}
