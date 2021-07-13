package com.silenteight.warehouse.report;

import lombok.SneakyThrows;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientFactory;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaTestClient;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.minio.MinioContainer.MinioContainerInitializer;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.synchronization.ReportDto;
import com.silenteight.warehouse.report.synchronization.ReportSynchronizationService;
import com.silenteight.warehouse.report.synchronization.ReportSynchronizationUseCase;

import io.minio.*;
import io.minio.messages.Item;
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
import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.SEARCH;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_KIBANA_INDEX_PATTERN_NAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.SAVED_SEARCH;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_ID_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@Transactional
@SpringBootTest(classes = ProductionPeriodicTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class,
    MinioContainerInitializer.class,
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
class ProductionPeriodicIT {

  private static final String PRODUCTION_PERIODIC_TENANT = "itest_production_periodic";
  private static final String TEST_BUCKET = "reports";

  @Autowired
  private ReportingService reportingService;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Autowired
  private OpendistroKibanaTestClient kibanaTestClient;

  @Autowired
  private MinioClient minioClient;

  @Autowired
  private ReportSynchronizationUseCase reportSynchronizationUseCase;

  @Autowired
  private ReportSynchronizationService reportSynchronizationService;

  @Autowired
  private OpendistroKibanaClientFactory opendistroKibanaClientFactory;

  @Autowired
  private OpendistroElasticClient opendistroElasticClient;

  private OpendistroKibanaClient opendistroKibanaClient;

  @BeforeEach
  public void init() {
    opendistroKibanaClient = opendistroKibanaClientFactory.getAdminClient();
    // given
    storeData();
    createProductionTenant();
    createKibanaIndex();
    createSavedSearch();
    generateReport(createReportDefinition());
    waitForReportInstances(1, PRODUCTION_PERIODIC_TENANT);
    createMinioBucket();
  }

  @AfterEach
  public void cleanup() {
    removeSavedSearch();
    removeKibanaIndex();
    removeReportDefinitions();
    clearMinioBucket();
    removeData();
  }

  @Test
  @SneakyThrows
  void shouldStoreNewKibanaReportsInMinio() {
    // when
    reportSynchronizationUseCase.activate();

    // then
    List<ReportDto> synchronizedReports = getSynchronizedReports();
    assertThat(synchronizedReports).hasSize(1);

    String reportFilename = synchronizedReports.get(0).getFilename();
    String reportContent = getReportContentFromMinio(reportFilename);
    assertThat(reportContent).contains(ALERT_ID_1);
  }

  @SneakyThrows
  private void storeData() {
    simpleElasticTestClient.storeData(
        PRODUCTION_ELASTIC_INDEX_NAME, ALERT_ID_1, MAPPED_ALERT_1);
  }

  private void createProductionTenant() {
    opendistroElasticClient.createTenant(PRODUCTION_PERIODIC_TENANT, "Production periodic reports");
  }

  @SneakyThrows
  private void createMinioBucket() {
    minioClient.makeBucket(MakeBucketArgs.builder().bucket(TEST_BUCKET).build());
  }

  private void createKibanaIndex() {
    byte[] payload = getJson("json/production/1-create-kibana-index.json");
    kibanaTestClient.createKibanaIndex(
        PRODUCTION_PERIODIC_TENANT, PRODUCTION_KIBANA_INDEX_PATTERN_NAME, payload);
  }

  private void createSavedSearch() {
    byte[] payload = getJson("json/production/2-create-saved-search.json");
    kibanaTestClient.createSavedSearch(PRODUCTION_PERIODIC_TENANT, payload);
  }

  private String createReportDefinition() {
    byte[] payload = getJson("json/3-create-report-definition.json");
    return kibanaTestClient.createReportDefinition(PRODUCTION_PERIODIC_TENANT, payload);
  }

  private void generateReport(String reportDefinitionId) {
    kibanaTestClient.generateReport(PRODUCTION_PERIODIC_TENANT, reportDefinitionId);
  }

  private void waitForReportInstances(int minCount, String tenant) {
    await()
        .atMost(5, SECONDS)
        .until(() -> reportingService.getReportIds(tenant).size() >= minCount);
  }

  @SneakyThrows
  private byte[] getJson(String resourcePath) {
    return getClass().getClassLoader().getResourceAsStream(resourcePath).readAllBytes();
  }

  private List<ReportDto> getSynchronizedReports() {
    return new ArrayList<>(
        reportSynchronizationService.getAllReportsForTenant(PRODUCTION_PERIODIC_TENANT));
  }

  @SneakyThrows
  private String getReportContentFromMinio(String filename) {
    GetObjectArgs getObjectArgs = GetObjectArgs.builder()
        .bucket(TEST_BUCKET)
        .object(filename)
        .build();
    byte[] responseBytes = minioClient.getObject(getObjectArgs).readAllBytes();
    return new String(responseBytes, UTF_8);
  }

  private void clearMinioBucket() {
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

  private void removeKibanaIndex() {
    opendistroKibanaClient.deleteSavedObjects(
        PRODUCTION_PERIODIC_TENANT, KIBANA_INDEX_PATTERN, PRODUCTION_KIBANA_INDEX_PATTERN_NAME);
  }

  private void removeSavedSearch() {
    opendistroKibanaClient.deleteSavedObjects(PRODUCTION_PERIODIC_TENANT, SEARCH, SAVED_SEARCH);
  }

  private void removeReportDefinitions() {
    opendistroKibanaClient.listReportDefinitions(PRODUCTION_PERIODIC_TENANT).forEach(
        report -> opendistroKibanaClient.deleteReportDefinition(
            PRODUCTION_PERIODIC_TENANT, report.getId())
    );
  }

  private void removeData() {
    simpleElasticTestClient.removeIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }
}
