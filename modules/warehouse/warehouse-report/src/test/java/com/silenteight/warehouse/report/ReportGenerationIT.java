package com.silenteight.warehouse.report;

import lombok.SneakyThrows;

import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.common.testing.e2e.CleanDatabase;
import com.silenteight.warehouse.common.testing.storage.minio.MinioContainer.MinioContainerInitializer;
import com.silenteight.warehouse.report.create.CreateReportRestController;
import com.silenteight.warehouse.report.create.ReportNotAvailableException;
import com.silenteight.warehouse.report.download.DownloadReportRestController;
import com.silenteight.warehouse.report.generation.ReportZipProperties;
import com.silenteight.warehouse.report.persistence.ReportNotFoundException;
import com.silenteight.warehouse.report.persistence.ReportStatus.Status;
import com.silenteight.warehouse.report.status.ReportStatusRestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import java.io.InputStream;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipInputStream;

import static com.silenteight.warehouse.report.ReportFixture.*;
import static java.sql.Timestamp.valueOf;
import static java.time.OffsetDateTime.parse;
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
@TestInstance(Lifecycle.PER_CLASS)
class ReportGenerationIT {

  private static final String TEST_BUCKET = "reports";
  private static final String TIMESTAMP_FROM = "2020-01-12T10:00:37.098Z";
  private static final String TIMESTAMP_TO = "2022-01-12T10:00:37.098Z";
  private static final String REPORT_NAME = "TEST_REPORT";
  private static final String REPORT_NAME_NOT_CONF = "TEST_REPORT_NOT_CONF";
  private static final String REPORT_TYPE = "production";
  private static final String USERNAME = "user";
  private static final String USERNAME_2 = "other_user";

  @Autowired
  private S3Client s3Client;

  @Autowired
  private CreateReportRestController createReportRestController;

  @Autowired
  private ReportStatusRestController reportStatusRestController;

  @Autowired
  private DownloadReportRestController downloadReportRestController;

  @Autowired
  private CountryPermissionService countryPermissionService;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  private ReportZipProperties reportZipProperties;

  @Autowired
  private Principal principal;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeAll
  void setUp() {
    createBucket();
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = COUNTRY_GROUP)
  void shouldGenerateReport() {
    storeData(DISCRIMINATOR_1, NAME_1, RECOMMENDATION_DATE, Map.of(
        PAYLOAD_KEY_SIGNATURE, PAYLOAD_VALUE_SIGNATURE,
        PAYLOAD_KEY_COMMENT, PAYLOAD_VALUE_COMMENT_LONG,
        PAYLOAD_KEY_COUNTRY, COUNTRY_PL));
    storeData(DISCRIMINATOR_2, NAME_2, RECOMMENDATION_DATE, Map.of(
        PAYLOAD_KEY_SIGNATURE, PAYLOAD_VALUE_SIGNATURE,
        PAYLOAD_KEY_COUNTRY, COUNTRY_US));
    when(countryPermissionService.getCountries(of(COUNTRY_GROUP)))
        .thenReturn(of(COUNTRY_PL));
    when(principal.getName()).thenReturn(USERNAME);

    Long instanceId = createReport();
    await()
        .atMost(5, SECONDS)
        .until(() -> isReportCreated(instanceId));
    String reportContent = getReportContent(instanceId);

    thenReportInDbHasCorrectExtension(instanceId, "CSV");
    assertThat(reportContent)
        .hasLineCount(3)
        .contains(
            "9HzsNs1bv",
            "PL",
            "TEST[AAAGLOBAL186R1038]_81596ace",
            """
              S8 recommended action: False PositivennSAN 2485278: Alerted Party's name (AO A C also known as ????????? | ???????????????) does not match Watchlist Party name (XXXYYY AO). Alerted Party's incorporation country (""\""CN"", CHINA, CN) does not match Watchlist Party country (XXXYYY, XXXYYY FEDERATION). Alerted Party's registration country (CHINA, ""\""CN"", CN) does not match Watchlist Party country (XXXYYY FEDERATION). Alerted Party's other countries (CHINA, CN) do not match Watchlist Party countries (XXXYYY, XXXYYY FEDERATION).
            """,
            "alerts/123",
            "2021-01-12 10:00:37");
  }


  @Test
  @WithMockUser(username = USERNAME, authorities = COUNTRY_GROUP)
  void shouldNotDownloadReportWhenOtherUserGenerated() {
    storeData(DISCRIMINATOR_1, NAME_1, RECOMMENDATION_DATE, Map.of(
        PAYLOAD_KEY_SIGNATURE, PAYLOAD_VALUE_SIGNATURE,
        PAYLOAD_KEY_COUNTRY, COUNTRY_PL));
    when(countryPermissionService.getCountries(of(COUNTRY_GROUP)))
        .thenReturn(of(COUNTRY_PL));
    when(principal.getName()).thenReturn(USERNAME);

    Long instanceId = createReport();
    await()
        .atMost(5, SECONDS)
        .until(() -> isReportCreated(instanceId));

    when(principal.getName()).thenReturn(USERNAME_2);
    assertThatThrownBy(() -> getReportContent(instanceId)).isInstanceOf(
        ReportNotFoundException.class);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = COUNTRY_GROUP)
  void shouldGenerateReportWithNewlineCharsInPayload() {
    storeData(DISCRIMINATOR_1, NAME_1, RECOMMENDATION_DATE, Map.of(
        PAYLOAD_KEY_SIGNATURE, PAYLOAD_VALUE_SIGNATURE,
        PAYLOAD_KEY_COUNTRY, COUNTRY_PL));
    storeData(DISCRIMINATOR_2, NAME_2, RECOMMENDATION_DATE, Map.of(
        PAYLOAD_KEY_COMMENT, PAYLOAD_VALUE_COMMENT_WITH_NEWLINE,
        PAYLOAD_KEY_SIGNATURE, PAYLOAD_VALUE_SIGNATURE,
        PAYLOAD_KEY_COUNTRY, COUNTRY_PL));
    when(countryPermissionService.getCountries(of(COUNTRY_GROUP)))
        .thenReturn(of(COUNTRY_PL));
    when(principal.getName()).thenReturn(USERNAME);

    Long instanceId = createReport();
    await()
        .atMost(5, SECONDS)
        .until(() -> isReportCreated(instanceId));
    String reportContent = getReportContent(instanceId);

    thenReportInDbHasCorrectExtension(instanceId, "CSV");
    assertThat(reportContent)
        .hasLineCount(5)
        .contains(NAME_1, NAME_2);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = COUNTRY_GROUP)
  void shouldIncludeOnlyAlertsWithSpecificRecommendationDate() {
    var payload = Map.of(PAYLOAD_KEY_COUNTRY, COUNTRY_PL);
    storeData(DISCRIMINATOR_1, NAME_1, toLocalDateTime(TIMESTAMP_FROM).minusSeconds(1), payload);
    storeData(DISCRIMINATOR_2, NAME_2, RECOMMENDATION_DATE, payload);
    storeData(DISCRIMINATOR_3, NAME_3, toLocalDateTime(TIMESTAMP_TO).plusSeconds(1), payload);
    when(countryPermissionService.getCountries(of(COUNTRY_GROUP)))
        .thenReturn(of(COUNTRY_PL));
    when(principal.getName()).thenReturn(USERNAME);

    Long instanceId = createReport();
    await()
        .atMost(5, SECONDS)
        .until(() -> isReportCreated(instanceId));
    String reportContent = getReportContent(instanceId);

    assertThat(reportContent)
        .hasLineCount(2)
        .contains(NAME_2);
  }

  private LocalDateTime toLocalDateTime(String dateTime) {
    return parse(dateTime).toLocalDateTime();
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = COUNTRY_GROUP)
  void shouldZipReportWhenHasMoreRowsThanRowsLimitParameter() {
    int givenRowsLimit = 1000;
    int givenReportRowsCount = 1200;
    when(reportZipProperties.isEnabled()).thenReturn(true);
    when(reportZipProperties.getRowsLimit()).thenReturn(givenRowsLimit);

    storeGivenAmountOfAlerts(givenReportRowsCount);
    when(countryPermissionService.getCountries(of(COUNTRY_GROUP)))
        .thenReturn(of(COUNTRY_PL));

    Long instanceId = createReport();
    await()
        .atMost(5, SECONDS)
        .until(() -> isReportCreated(instanceId));

    var reportContent = getReportZipped(instanceId);
    var zippedFiles = collectZipEntries(reportContent);

    thenReportInDbHasCorrectExtension(instanceId, "ZIP");
    assertThat(zippedFiles).hasSize(2);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = COUNTRY_GROUP)
  void shouldNotGenerateReportWhenReportConfigurationNotPresent() {
    when(countryPermissionService.getCountries(of(COUNTRY_GROUP)))
        .thenReturn(of(COUNTRY_PL));
    when(principal.getName()).thenReturn(USERNAME);

    assertThatThrownBy(() -> createReportRestController.createReport(
        parse(TIMESTAMP_FROM),
        parse(TIMESTAMP_TO),
        REPORT_TYPE,
        REPORT_NAME_NOT_CONF, principal))
        .isInstanceOf(ReportNotAvailableException.class);
  }

  @SneakyThrows
  private Long createReport() {
    when(principal.getName()).thenReturn(USERNAME);
    ResponseEntity<Void> report = createReportRestController.createReport(
        parse(TIMESTAMP_FROM),
        parse(TIMESTAMP_TO),
        REPORT_TYPE,
        REPORT_NAME, principal);

    String location = report.getHeaders().get("Location").get(0);
    return Long.valueOf(location.replaceAll("[^0-9]", ""));
  }

  private void thenReportInDbHasCorrectExtension(Long instanceId, String expectedExtension) {
    String query = "SELECT extension FROM warehouse_report WHERE id = :ID";
    String reportExtension = namedParameterJdbcTemplate.queryForObject(query,
        Map.of("ID", instanceId), String.class);
    assertThat(reportExtension).isEqualTo(expectedExtension);
  }

  private boolean isReportCreated(Long reportId) {
    return Status.OK == reportStatusRestController.checkReportStatus(
            REPORT_TYPE, REPORT_NAME, reportId, principal).getBody()
        .getStatus();
  }

  @SneakyThrows
  private String getReportContent(Long instanceId) {
    return new String(downloadReportRestController
        .downloadReport(REPORT_TYPE, REPORT_NAME, instanceId, principal)
        .getBody()
        .getInputStream()
        .readAllBytes());
  }

  @SneakyThrows
  private ZipInputStream getReportZipped(Long instanceId) {
    var inputStream = downloadReportRestController
        .downloadReport(REPORT_TYPE, REPORT_NAME, instanceId, principal)
        .getBody()
        .getInputStream();
    return new ZipInputStream(inputStream);
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

  private void storeGivenAmountOfAlerts(int givenReportRowsCount) {
    for (int i = 0; i < givenReportRowsCount; i++) {
      storeData(
          UUID.randomUUID().toString(), UUID.randomUUID().toString(), RECOMMENDATION_DATE,
          Map.of(PAYLOAD_KEY_SIGNATURE, PAYLOAD_VALUE_SIGNATURE,
              PAYLOAD_KEY_COUNTRY, COUNTRY_PL));
    }
  }

  @SneakyThrows
  private List<InputStream> collectZipEntries(ZipInputStream reportContent) {
    var entries = new ArrayList<InputStream>();
    while (reportContent.getNextEntry() != null) {
      entries.add(reportContent);
    }
    return entries;
  }

  @SneakyThrows
  private void createBucket() {
    s3Client.createBucket(CreateBucketRequest.builder().bucket(TEST_BUCKET).build());
  }
}
