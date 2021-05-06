package com.silenteight.warehouse.report;

import lombok.SneakyThrows;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaTestClient;
import com.silenteight.warehouse.common.opendistro.kibana.dto.SavedObjectDto;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.common.testing.minio.MinioContainer.MinioContainerInitializer;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.synchronization.ReportDto;
import com.silenteight.warehouse.report.synchronization.ReportSynchronizationService;
import com.silenteight.warehouse.report.synchronization.ReportSynchronizationUseCase;
import com.silenteight.warehouse.report.tenant.TenantService;

import io.minio.*;
import io.minio.messages.Item;
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

import java.util.ArrayList;
import java.util.List;

import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.INDEX_NAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.KIBANA_INDEX_PATTERN_NAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.OTHER_TENANT;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.TENANT;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_ID_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_WITH_MATCHES_1_MAP;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Transactional
@SpringBootTest(classes = ReportTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class,
    MinioContainerInitializer.class,
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
class ReportIT {

  private static final String TEST_BUCKET = "reports";
  private static final String NEW_ELASTIC_INDEX = "analysis-2";

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
  private MinioClient minioClient;

  @Autowired
  private ReportSynchronizationUseCase reportSynchronizationUseCase;

  @Autowired
  private ReportSynchronizationService reportSynchronizationService;

  @Autowired
  private TenantService tenantService;

  @Autowired
  private OpendistroKibanaClient opendistroKibanaClient;

  @BeforeEach
  public void init() {
    storeData();
    createKibanaIndex();
  }

  @Test
  @SneakyThrows
  void shouldCopyKibanaReports() {
    var indexMapping = tenantService.copyKibanaIndices(TENANT, OTHER_TENANT, NEW_ELASTIC_INDEX);

    var tenants = opendistroKibanaClient
        .listSavedObjects(OTHER_TENANT, KIBANA_INDEX_PATTERN, 10);

    assertThat(tenants).hasSize(1);
    SavedObjectDto kibanaIndex = tenants.get(0);
    assertThat(kibanaIndex.getId()).isEqualTo(indexMapping.get(KIBANA_INDEX_PATTERN_NAME));
    assertThat(kibanaIndex.getAttributes()).containsEntry("title", NEW_ELASTIC_INDEX);
  }

  @Test
  @SneakyThrows
  void shouldStoreNewKibanaReportsInMinio() {
    // given
    createSavedSearch();
    generateReport(createReportDefinition());
    waitForReports();

    //when
    reportSynchronizationUseCase.activate();

    //then
    List<ReportDto> synchronizedReports = getSynchronizedReports();
    assertThat(synchronizedReports).hasSize(1);

    String reportFilename = synchronizedReports.get(0).getFilename();
    String reportContent = getReportContentFromMinio(reportFilename);
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

  @SneakyThrows
  private void createMinioBucket() {
    minioClient.makeBucket(MakeBucketArgs.builder().bucket(TEST_BUCKET).build());
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

  private List<ReportDto> getSynchronizedReports() {
    return new ArrayList<>(reportSynchronizationService.getAllReportsForTenant(TENANT));
  }

  @SneakyThrows
  private String getReportContentFromMinio(String filename) {
    byte[] responseBytes = minioClient.getObject(GetObjectArgs.builder()
        .bucket(TEST_BUCKET)
        .object(filename)
        .build())
        .readAllBytes();
    return new String(responseBytes, UTF_8);
  }

  void clearMinioBucket() {
    removeAllReportsFromBucket();
    removeMinioBucket();
  }

  @SneakyThrows
  private void removeAllReportsFromBucket() {
    Iterable<Result<Item>> objects = minioClient.listObjects(
        ListObjectsArgs.builder().bucket(TEST_BUCKET).build());

    for (Result<Item> obj : objects) {
      String objectName = obj.get().objectName();
      minioClient.removeObject(
          RemoveObjectArgs.builder().bucket(TEST_BUCKET).object(objectName).build());
    }
  }

  @SneakyThrows
  private void removeMinioBucket() {
    minioClient.removeBucket(RemoveBucketArgs.builder().bucket(TEST_BUCKET).build());
  }
}
