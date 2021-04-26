package com.silenteight.warehouse.report;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaTestClient;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.common.testing.minio.MinioContainer.MinioContainerInitializer;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.storage.InMemoryReport;
import com.silenteight.warehouse.report.storage.ReportStorageService;

import io.minio.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.INDEX_NAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.TENANT;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_ID_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_WITH_MATCHES_1_MAP;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = ReportTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class,
    MinioContainerInitializer.class
})
class ReportIT {

  private static final String REPORT_NAME = "test-report";
  private static final String TEST_BUCKET = "reports";

  @BeforeEach
  void setUp() {
    createMinioBucket();
  }

  @AfterEach
  void tearDown() {
    clearMinioBucket();
  }

  @Autowired
  private ReportingService reportingService;

  @Autowired
  private RestHighLevelClient restHighLevelClient;

  @Autowired
  private OpendistroKibanaTestClient kibanaTestClient;

  @Autowired
  MinioClient minioClient;

  @Autowired
  ReportStorageService reportStorageService;

  @SneakyThrows
  @Test
  void shouldDownloadGeneratedReport() {
    storeData();
    createKibanaIndex();
    createSavedSearch();
    generateReport(createReportDefinition());
    waitForReports();

    String reportInstanceId = reportingService.getReportList(TENANT).get(0);
    String reportContent = reportingService.getReportContent(TENANT, reportInstanceId);

    assertThat(reportContent).contains(ALERT_ID_1);
  }

  @SneakyThrows
  private void storeData() {
    IndexRequest indexRequest = new IndexRequest(INDEX_NAME);
    indexRequest.id(ALERT_ID_1);
    indexRequest.source(ALERT_WITH_MATCHES_1_MAP);

    indexRequest.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
    restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
  }

  @Test
  @SneakyThrows
  void shouldDownloadCorrectData() {
    //given
    byte[] testBytes = { 116, 101, 115, 116, 32, 100, 97, 116, 97 };
    InMemoryReport report = new InMemoryReport(REPORT_NAME, testBytes);

    //when
    reportStorageService.saveReport(report);

    //then
    byte[] responseBytes = minioClient.getObject(
        GetObjectArgs.builder().bucket(TEST_BUCKET).object(REPORT_NAME).build()).readAllBytes();

    assertThat(responseBytes).isEqualTo(testBytes);
  }

  @SneakyThrows
  private void createMinioBucket() {
    minioClient.makeBucket(MakeBucketArgs.builder().bucket(TEST_BUCKET).build());
  }

  void clearMinioBucket() {
    removeReportFromBucket();
    removeMinioBucket();
  }

  @SneakyThrows
  private void removeReportFromBucket() {
    minioClient.removeObject(
        RemoveObjectArgs.builder().bucket(TEST_BUCKET).object(REPORT_NAME).build());
  }

  @SneakyThrows
  private void removeMinioBucket() {
    minioClient.removeBucket(RemoveBucketArgs.builder().bucket(TEST_BUCKET).build());
  }

  private void createKibanaIndex() {
    byte[] payload = getJson("json/1-create-kibana-index.json");
    kibanaTestClient.createKibanaIndex(TENANT, payload);
  }

  private void createSavedSearch() {
    byte[] payload = getJson("json/2-create-saved-search.json");
    kibanaTestClient.createSavedSearch(TENANT, payload);
  }

  private String createReportDefinition() {
    byte[] payload = getJson("json/3-create-report-definition.json");
    return kibanaTestClient.createReportDefinition(TENANT, payload);
  }

  private void generateReport(String reportDefinitionId) {
    kibanaTestClient.generateReport(TENANT, reportDefinitionId);
  }

  private void waitForReports() {
    await()
        .atMost(5, SECONDS)
        .until(() -> reportingService.getReportList(TENANT).size() > 0);
  }

  @SneakyThrows
  private byte[] getJson(String resourcePath) {
    return getClass().getClassLoader().getResourceAsStream(resourcePath).readAllBytes();
  }
}
