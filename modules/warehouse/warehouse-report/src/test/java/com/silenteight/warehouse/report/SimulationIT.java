package com.silenteight.warehouse.report;

import lombok.SneakyThrows;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaTestClient;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.indexer.analysis.AnalysisMetadataDto;
import com.silenteight.warehouse.indexer.analysis.NewAnalysisEvent;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.simulation.KibanaSetupForSimulationUseCase;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.SEARCH;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.*;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_ID_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_WITH_MATCHES_1_MAP;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

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
  private static final NewAnalysisEvent ITEST_ANALYSIS_EVENT = NewAnalysisEvent.builder()
      .analysis("analysis/" + SIMULATION_ANALYSIS_ID)
      .simulation(true)
      .analysisMetadataDto(ITEST_ANALYSIS)
      .build();

  @Autowired
  private ReportingService reportingService;

  @Autowired
  private RestHighLevelClient restHighLevelClient;

  @Autowired
  private OpendistroKibanaTestClient kibanaTestClient;

  @Autowired
  private OpendistroKibanaClient opendistroKibanaClient;

  @Autowired
  private KibanaSetupForSimulationUseCase kibanaSetupForSimulationUseCase;

  @BeforeEach
  public void init() {
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
  void shouldCreateReportForSimulation() {
    //when
    kibanaSetupForSimulationUseCase.handle(ITEST_ANALYSIS_EVENT);

    //then
    var clonedIndexPatterns = opendistroKibanaClient.listKibanaIndexPattern(
        SIMULATION_TENANT, 20);
    assertThat(clonedIndexPatterns).hasSize(1);

    var clonedSearchObjects = opendistroKibanaClient.listSavedSearchDefinitions(
        SIMULATION_TENANT, 20);
    assertThat(clonedSearchObjects).hasSize(1);

    // TODO(WEB-1070): to be replaced by REST API call:
    // GET /analysis/{analysisId}/definitions
    waitForReportDefinitions(1, SIMULATION_TENANT);
    var reportDefinitions = opendistroKibanaClient.listReportDefinitions(SIMULATION_TENANT);
    assertThat(reportDefinitions).hasSize(1);

    // TODO(WEB-1070): to be replaced by REST API call:
    // POST /analysis/production/definitions/{definitionId}/reports
    reportingService.createReport(reportDefinitions.get(0).getId(), SIMULATION_TENANT);

    // TODO(WEB-1070): to be replaced by REST API call:
    // GET /analysis/production/definitions/{definitionId}/reports/{instanceId}
    waitForReportInstances(1, SIMULATION_TENANT);
    String reportId = getFirstReportInstance(SIMULATION_TENANT);
    KibanaReportDto report = reportingService.getReport(SIMULATION_TENANT, reportId);
    assertThat(report.getContent()).contains(ALERT_ID_1);
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

  private String createReportDefinition() {
    byte[] payload = getJson("json/3-create-report-definition.json");
    return kibanaTestClient.createReportDefinition(ADMIN_TENANT, payload);
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

  private String getFirstReportInstance(String tenant) {
    Set<String> reportIds = reportingService.getReportIds(tenant);
    return reportIds.toArray(String[]::new)[0];
  }

  @SneakyThrows
  private byte[] getJson(String resourcePath) {
    return getClass().getClassLoader().getResourceAsStream(resourcePath).readAllBytes();
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
