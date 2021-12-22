package com.silenteight.warehouse.report;

import lombok.SneakyThrows;

import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.filestorage.api.StorageManager;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.sep.filestorage.minio.container.MinioContainer.MinioContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.report.reporting.ReportGenerationService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_5;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_5;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.COUNTRY_PL;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.COUNTRY_UK;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.RECOMMENDATION_FP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.RECOMMENDATION_MI;
import static com.silenteight.warehouse.report.ReportFixture.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    MinioContainerInitializer.class,
    PostgresTestInitializer.class
}, classes = ReportGenerationConfiguration.class)
@ImportAutoConfiguration({
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@DataJpaTest
@EnableAutoConfiguration
class ReportGenerationIT {

  private static final String TEST_BUCKET = "report";

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Autowired
  private StorageManager storageManager;

  @Autowired
  private ReportGenerationService generationService;

  @Autowired
  private ReportStorage reportStorage;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  public void init() {
    // given
    storeData();
    createMinioBucket();
  }

  @AfterEach
  public void cleanup() {
    clearMinioBucket();
    removeData();
  }

  @Test
  @SneakyThrows
  @WithElasticAccessCredentials
  void shouldStoreNewReportsInMinio() {
    // when
    generationService.generate(FETCH_DATA_REQUEST);

    // then
    String reportContent = getReportContentFromMinio(FILE_NAME);
    assertThat(reportContent)
        .contains(COUNTRY_UK, RECOMMENDATION_FP, DISCRIMINATOR_1)
        .doesNotContain(COUNTRY_PL, RECOMMENDATION_MI, DISCRIMINATOR_5);
  }

  @Test
  @SneakyThrows
  @WithElasticAccessCredentials
  void shouldStoreSqlReportsInMinio() {
    // when
    generationService.generate(FETCH_DATA_REQUEST_SQL);

    // then
    String reportContent = getReportContentFromMinio(FILE_NAME);
    assertThat(reportContent).isNotEmpty();
  }

  @SneakyThrows
  private void storeData() {
    simpleElasticTestClient.storeData(
        PRODUCTION_ELASTIC_INDEX_NAME, DISCRIMINATOR_1, MAPPED_ALERT_1);

    simpleElasticTestClient.storeData(
        PRODUCTION_ELASTIC_INDEX_NAME, DISCRIMINATOR_5, MAPPED_ALERT_5);

    entityManager.getEntityManager()
        .createNativeQuery("INSERT INTO "
            + "warehouse_alert(discriminator, name, recommendation_date, payload) "
            + "VALUES (?, ?, ?, TO_JSON( ? ))")
        .setParameter(1, "81596ace4ed087762f74962450bf1b57")
        .setParameter(2, "TEST[ANO][CUSTOMER][AAAGLOBAL186R1038]")
        .setParameter(3, LOCAL_DATE_TO)
        .setParameter(4, PAYLOAD_DATA)
        .executeUpdate();
  }

  @SneakyThrows
  private void createMinioBucket() {
    storageManager.create(TEST_BUCKET);
  }

  @SneakyThrows
  private String getReportContentFromMinio(String reportName) {
    FileDto report = reportStorage.getReport(reportName);
    byte[] responseBytes = report.getContent().readAllBytes();
    return new String(responseBytes, UTF_8);
  }

  private void clearMinioBucket() {
    reportStorage.removeReport(FILE_NAME);
    storageManager.remove(TEST_BUCKET);
  }

  private void removeData() {
    simpleElasticTestClient.removeIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }
}
