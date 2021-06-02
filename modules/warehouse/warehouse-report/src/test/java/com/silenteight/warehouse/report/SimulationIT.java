package com.silenteight.warehouse.report;

import lombok.SneakyThrows;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientFactory;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaTestClient;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.indexer.analysis.AnalysisMetadataDto;
import com.silenteight.warehouse.indexer.analysis.NewSimulationAnalysisEvent;
import com.silenteight.warehouse.indexer.analysis.SimulationAnalysisService;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.simulation.KibanaSetupForSimulationUseCase;
import com.silenteight.warehouse.report.simulation.SimulationReportsController;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.SEARCH;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.*;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_ID_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_WITH_MATCHES_1_MAP;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest(classes = SimulationTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class,
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
class SimulationIT {

  private static final String SIMULATION_TENANT = "itest_simulation_" + SIMULATION_ANALYSIS_ID;
  private static final AnalysisMetadataDto ITEST_ANALYSIS = AnalysisMetadataDto.builder()
      .tenant(SIMULATION_TENANT)
      .elasticIndexName(ELASTIC_INDEX_NAME)
      .build();
  private static final NewSimulationAnalysisEvent ITEST_ANALYSIS_EVENT =
      NewSimulationAnalysisEvent.builder()
          .analysis(SIMULATION_ANALYSIS)
          .analysisMetadataDto(ITEST_ANALYSIS)
          .build();

  @Autowired
  private ReportingService reportingService;

  @Autowired
  private RestHighLevelClient restHighLevelClient;

  @Autowired
  private OpendistroKibanaTestClient kibanaTestClient;

  @Autowired
  private OpendistroKibanaClientFactory opendistroKibanaClientFactory;

  private OpendistroKibanaClient opendistroKibanaClient;

  @Autowired
  private KibanaSetupForSimulationUseCase kibanaSetupForSimulationUseCase;

  @Autowired
  private SimulationReportsController simulationReportsController;

  @Autowired
  private SimulationAnalysisService simulationAnalysisService;

  @BeforeEach
  public void init() {
    opendistroKibanaClient = opendistroKibanaClientFactory.getAdminClient();
    // given
    storeData();
    createKibanaIndex();
    createSavedSearch();
    createReportDefinition();
    waitForReportDefinitions(1, ADMIN_TENANT);
  }

  @AfterEach
  public void cleanup() {
    removeSavedSearch();
    removeKibanaIndex();
    removeReportDefinitions();
  }

  @Test
  @WithMockUser(username = "admin", password = "admin")
  void shouldCreateReportForSimulation() {
    when(simulationAnalysisService.getTenantIdByAnalysis(SIMULATION_ANALYSIS))
        .thenReturn(SIMULATION_TENANT);

    // Trigger creating tenant for simulation
    kibanaSetupForSimulationUseCase.handle(ITEST_ANALYSIS_EVENT);

    // Verify tenant is cloned
    verifyClonedIndexPattern();
    verifySavedSearch();

    // Trigger report generation
    waitForReportDefinitions(1, SIMULATION_TENANT);
    String reportDefinitionId = getFirstReportDefinitionId(SIMULATION_ANALYSIS_ID);
    String timestamp = generateReport(SIMULATION_ANALYSIS_ID, reportDefinitionId);

    // Download report and verify the content
    waitForReportInstances(1, SIMULATION_TENANT);
    String report = downloadReport(SIMULATION_ANALYSIS_ID, reportDefinitionId, timestamp);
    assertThat(report).contains(ALERT_ID_1);
  }

  @SneakyThrows
  private void storeData() {
    IndexRequest indexRequest = new IndexRequest(ELASTIC_INDEX_NAME);
    indexRequest.id(ALERT_ID_1);
    indexRequest.source(ALERT_WITH_MATCHES_1_MAP);

    indexRequest.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
    restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
  }

  private void createKibanaIndex() {
    byte[] payload = getJson("json/1-create-kibana-index.json");
    kibanaTestClient.createKibanaIndex(ADMIN_TENANT, payload);
  }

  private void createSavedSearch() {
    byte[] payload = getJson("json/2-create-saved-search.json");
    kibanaTestClient.createSavedSearch(ADMIN_TENANT, payload);
  }

  private void createReportDefinition() {
    byte[] payload = getJson("json/3-create-report-definition.json");
    kibanaTestClient.createReportDefinition(ADMIN_TENANT, payload);
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

  private void verifyClonedIndexPattern() {
    var clonedIndexPatterns = opendistroKibanaClient.listKibanaIndexPattern(
        SIMULATION_TENANT, 20);
    assertThat(clonedIndexPatterns).hasSize(1);
  }

  private void verifySavedSearch() {
    var clonedSearchObjects = opendistroKibanaClient.listSavedSearchDefinitions(
        SIMULATION_TENANT, 20);
    assertThat(clonedSearchObjects).hasSize(1);
  }

  private String getFirstReportDefinitionId(String analysisId) {
    return simulationReportsController
        .getReportsDtoList(analysisId)
        .getBody()
        .get(0)
        .getId();
  }

  private String generateReport(String analysisId, String reportDefinitionId) {
    String location =
        simulationReportsController.createReport(analysisId, reportDefinitionId)
            .getHeaders()
            .get("Location")
            .get(0);
    return location.replace("reports/", "");
  }

  String downloadReport(String analysisId, String definitionId, String timestamp) {
    byte[] reportBody =
        simulationReportsController.downloadReport(analysisId, definitionId, timestamp)
            .getBody();

    return new String(reportBody);
  }

  private void removeKibanaIndex() {
    opendistroKibanaClient.deleteSavedObjects(
        ADMIN_TENANT, KIBANA_INDEX_PATTERN, KIBANA_INDEX_PATTERN_NAME);
  }

  private void removeSavedSearch() {
    opendistroKibanaClient.deleteSavedObjects(ADMIN_TENANT, SEARCH, SAVED_SEARCH);
  }

  private void removeReportDefinitions() {
    opendistroKibanaClient.listReportDefinitions(ADMIN_TENANT).forEach(
        report -> opendistroKibanaClient.deleteReportDefinition(ADMIN_TENANT, report.getId())
    );
  }
}
