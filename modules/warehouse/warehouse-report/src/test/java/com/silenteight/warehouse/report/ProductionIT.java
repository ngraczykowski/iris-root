package com.silenteight.warehouse.report;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientFactory;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaTestClient;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.report.production.ProductionReportType;
import com.silenteight.warehouse.report.production.ProductionReportsRestController;
import com.silenteight.warehouse.report.reporting.ReportingService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.SEARCH;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_KIBANA_INDEX_PATTERN_NAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.SAVED_SEARCH;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_ID_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_WITH_MATCHES_1_MAP;
import static com.silenteight.warehouse.report.production.ProductionReportType.ACCURACY;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest(classes = ProductionTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class
})
class ProductionIT {

  private static final ProductionReportType REPORT_TYPE = ACCURACY;
  private static final String PRODUCTION_TENANT = REPORT_TYPE.getTenantName("itest");

  @Autowired
  private ReportingService reportingService;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Autowired
  private OpendistroKibanaTestClient kibanaTestClient;

  @Autowired
  private OpendistroKibanaClientFactory opendistroKibanaClientFactory;

  private OpendistroKibanaClient opendistroKibanaClient;

  @Autowired
  private OpendistroElasticClient opendistroElasticClient;

  @Autowired
  private ProductionReportsRestController productionReportsRestController;

  @BeforeEach
  public void init() {
    opendistroKibanaClient = opendistroKibanaClientFactory.getAdminClient();
    // given
    log.info("Setting up ES and Kibana");
    storeData();
    createProductionTenant();
    createKibanaIndex();
    createSavedSearch();
    createReportDefinition();
  }

  @AfterEach
  public void cleanup() {
    log.info("Cleaning up ES and Kibana");
    removeSavedSearch();
    removeKibanaIndex();
    removeReportDefinitions();
    removeData();
  }

  @Test
  @WithElasticAccessCredentials
  void shouldCreateReportForProduction() {
    // Trigger report generation
    waitForReportDefinitions(1, PRODUCTION_TENANT);
    String reportDefinitionId = getFirstReportDefinitionId(REPORT_TYPE);
    String timestamp = generateReport(REPORT_TYPE, reportDefinitionId);

    // Download report and verify the content
    waitForReportInstances(1, PRODUCTION_TENANT);
    String report = downloadReport(reportDefinitionId, timestamp, REPORT_TYPE);
    assertThat(report).contains(ALERT_ID_1);
  }

  @SneakyThrows
  private void storeData() {
    simpleElasticTestClient.storeData(
        PRODUCTION_ELASTIC_INDEX_NAME, ALERT_ID_1, ALERT_WITH_MATCHES_1_MAP);
  }

  private void createProductionTenant() {
    opendistroElasticClient.createTenant(PRODUCTION_TENANT, "Production reports");
  }

  private void createKibanaIndex() {
    byte[] payload = getJson("json/production/1-create-kibana-index.json");
    kibanaTestClient.createKibanaIndex(
        PRODUCTION_TENANT, PRODUCTION_KIBANA_INDEX_PATTERN_NAME, payload);
  }

  private void createSavedSearch() {
    byte[] payload = getJson("json/production/2-create-saved-search.json");
    kibanaTestClient.createSavedSearch(PRODUCTION_TENANT, payload);
  }

  private void createReportDefinition() {
    byte[] payload = getJson("json/3-create-report-definition.json");
    kibanaTestClient.createReportDefinition(PRODUCTION_TENANT, payload);
  }

  private void waitForReportInstances(int minCount, String tenant) {
    await()
        .atMost(5, SECONDS)
        .until(() -> reportingService.getReportIds(tenant).size() >= minCount);
  }

  private void waitForReportDefinitions(int minCount, String tenant) {
    await()
        .atMost(5, SECONDS)
        .until(() -> opendistroKibanaClient.listReportDefinitions(tenant).size() >= minCount);
  }

  @SneakyThrows
  private byte[] getJson(String resourcePath) {
    return getClass().getClassLoader().getResourceAsStream(resourcePath).readAllBytes();
  }

  private String getFirstReportDefinitionId(ProductionReportType reportType) {
    return productionReportsRestController
        .getProductionReportDefinitionDto(reportType)
        .getBody()
        .get(0)
        .getId();
  }

  private String generateReport(ProductionReportType reportType, String reportDefinitionId) {
    String location =
        productionReportsRestController.createReport(reportDefinitionId, reportType)
            .getHeaders()
            .get("Location")
            .get(0);
    return location.replace("reports/", "").replace("/status", "");
  }

  String downloadReport(String definitionId, String timestamp, ProductionReportType reportType) {
    byte[] reportBody =
        productionReportsRestController.downloadReport(definitionId, timestamp, reportType)
            .getBody();

    return Optional.ofNullable(reportBody).map(String::new).orElse(null);
  }

  private void removeKibanaIndex() {
    opendistroKibanaClient.deleteSavedObjects(
        PRODUCTION_TENANT, KIBANA_INDEX_PATTERN, PRODUCTION_KIBANA_INDEX_PATTERN_NAME);
  }

  private void removeSavedSearch() {
    opendistroKibanaClient.deleteSavedObjects(PRODUCTION_TENANT, SEARCH, SAVED_SEARCH);
  }

  private void removeReportDefinitions() {
    opendistroKibanaClient.listReportDefinitions(PRODUCTION_TENANT).forEach(
        report -> opendistroKibanaClient.deleteReportDefinition(PRODUCTION_TENANT, report.getId())
    );
  }

  private void removeData() {
    simpleElasticTestClient.removeIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }
}
