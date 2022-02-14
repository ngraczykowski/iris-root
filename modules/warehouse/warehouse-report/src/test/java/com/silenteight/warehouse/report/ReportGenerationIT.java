package com.silenteight.warehouse.report;

import lombok.SneakyThrows;

import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.filestorage.api.StorageManager;
import com.silenteight.sep.filestorage.minio.container.MinioContainer.MinioContainerInitializer;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.common.testing.e2e.CleanDatabase;
import com.silenteight.warehouse.report.create.CreateReportRestController;
import com.silenteight.warehouse.report.create.ReportNotAvailableException;
import com.silenteight.warehouse.report.download.DownloadReportRestController;
import com.silenteight.warehouse.report.persistence.ReportStatus.Status;
import com.silenteight.warehouse.report.status.ReportStatusRestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

import static com.silenteight.warehouse.report.ReportFixture.*;
import static java.sql.Timestamp.valueOf;
import static java.util.Set.of;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@ContextConfiguration(initializers = {
    MinioContainerInitializer.class,
    PostgresTestInitializer.class
}, classes = ReportGenerationConfiguration.class)
@ImportAutoConfiguration({
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@DataJpaTest
@EnableAutoConfiguration
@Transactional(propagation = NOT_SUPPORTED)
@CleanDatabase
class ReportGenerationIT {

  private static final String TEST_BUCKET = "report";
  private static final String TIMESTAMP_FROM = "2020-01-12T10:00:37.098Z";
  private static final String TIMESTAMP_TO = "2022-01-12T10:00:37.098Z";
  private static final String REPORT_NAME = "TEST_REPORT";
  private static final String REPORT_NAME_NOT_CONF = "TEST_REPORT_NOT_CONF";
  private static final String REPORT_TYPE = "production";

  @Autowired
  private CreateReportRestController createReportRestController;

  @Autowired
  private ReportStatusRestController reportStatusRestController;

  @Autowired
  private DownloadReportRestController downloadReportRestController;

  @Autowired
  private StorageManager storageManager;

  @Autowired
  private CountryPermissionService countryPermissionService;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @WithMockUser(username = "user", authorities = COUNTRY_GROUP)
  void shouldGenerateReport() {
    createMinioBucket();
    storeData(DISCRIMINATOR_1, NAME_1, RECOMMENDATION_DATE, Map.of(
        PAYLOAD_KEY_SIGNATURE, PAYLOAD_VALUE_SIGNATURE,
        PAYLOAD_KEY_COUNTRY, COUNTRY_PL));
    storeData(DISCRIMINATOR_2, NAME_2, RECOMMENDATION_DATE, Map.of(
        PAYLOAD_KEY_SIGNATURE, PAYLOAD_VALUE_SIGNATURE,
        PAYLOAD_KEY_COUNTRY, COUNTRY_US));
    when(countryPermissionService.getCountries(of(COUNTRY_GROUP)))
        .thenReturn(of(COUNTRY_PL));

    Long instanceId = createReport();
    await()
        .atMost(5, SECONDS)
        .until(() -> isReportCreated(instanceId));
    String reportContent = getReportContent(instanceId);

    assertThat(reportContent)
        .hasLineCount(2)
        .contains("9HzsNs1bv,PL,TEST[AAAGLOBAL186R1038]_81596ace,alerts/123,2021-01-12 10:00:37");
  }

  @Test
  @SneakyThrows
  @WithMockUser(username = "user", authorities = COUNTRY_GROUP)
  void shouldNotGenerateReportWhenNotConfigured() {
    when(countryPermissionService.getCountries(of(COUNTRY_GROUP)))
        .thenReturn(of(COUNTRY_PL));

    assertThatThrownBy(() -> createReportRestController.createReport(
        OffsetDateTime.parse(TIMESTAMP_FROM),
        OffsetDateTime.parse(TIMESTAMP_TO),
        REPORT_TYPE,
        REPORT_NAME_NOT_CONF))
        .isInstanceOf(ReportNotAvailableException.class);
  }

  @SneakyThrows
  private Long createReport() {
    ResponseEntity<Void> report = createReportRestController.createReport(
        OffsetDateTime.parse(TIMESTAMP_FROM),
        OffsetDateTime.parse(TIMESTAMP_TO),
        REPORT_TYPE,
        REPORT_NAME);

    String location = report.getHeaders().get("Location").get(0);
    return Long.valueOf(location.replaceAll("[^0-9]", ""));
  }

  private boolean isReportCreated(Long reportId) {
    return Status.OK == reportStatusRestController.checkReportStatus(
        REPORT_TYPE, REPORT_NAME, reportId).getBody()
        .getStatus();
  }

  @SneakyThrows
  private String getReportContent(Long instanceId) {
    return new String(downloadReportRestController
        .downloadReport(REPORT_TYPE, REPORT_NAME, instanceId)
        .getBody()
        .getInputStream()
        .readAllBytes());
  }

  @SneakyThrows
  private void storeData(
      String discriminator, String alertName,
      LocalDateTime utcRecommendationDate, Map<String, String> payload) {

    String serializedPayload = objectMapper.writeValueAsString(payload);

    namedParameterJdbcTemplate.update(
        "INSERT INTO "
            + "warehouse_alert(discriminator, name, recommendation_date, payload) "
            + "VALUES (:discriminator, :name, :recommendation_date, TO_JSON(:payload::jsonb))",
        Map.of(
            "discriminator", discriminator,
            "name", alertName,
            "recommendation_date", valueOf(utcRecommendationDate),
            "payload", serializedPayload));
  }

  @SneakyThrows
  private void createMinioBucket() {
    storageManager.create(TEST_BUCKET);
  }

  @SneakyThrows
  private void removeMinioBucket() {
    storageManager.create(TEST_BUCKET);
  }
}
