package com.silenteight.payments.bridge.app.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.payments.bridge.PaymentsBridgeApplication;
import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.alertmessage.port.CreateAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.dto.input.RequestDto;
import com.silenteight.payments.bridge.mock.ae.MockAlertUseCase;
import com.silenteight.payments.bridge.mock.datasource.MockDatasourceService;
import com.silenteight.payments.bridge.mock.warehouse.MockWarehouseService;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import org.awaitility.core.ConditionEvaluationLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.IdGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import javax.mail.internet.MimeMessage;

import static com.silenteight.payments.bridge.testing.GreenMailConfiguration.getServerStartup;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = { PaymentsBridgeApplication.class })
@ContextConfiguration(initializers = { RabbitTestInitializer.class, PostgresTestInitializer.class })
@Slf4j
@ActiveProfiles({
    "mockae", "mockdatasource", "mockgovernance", "mockagents", "mockaws", "test",
    "mockwarehouse" })
class PaymentsBridgeApplicationIT {

  private static long databaseId = 1;
  private static final String SAMPLE_REQUESTS_DIR = "requests";
  private static final List<String> VALID_REQUEST_FILES = List.of(
      "2021_10_08-1754_uat_firco_alert.json",
      "gtex_firco_alert.json", "h_r_gtex_firco_alert.json");

  private static final String TOO_MANY_HITS_REQUEST_FILE =
      "2021-10-01_1837_osama_bin_laden.json";

  private static final String EMAIL_RECEIVER = "email.example2@silenteight.com";
  private static final String EMAIL_SUBJECT = "Silent Eight - Learning data";
  private static final String EMAIL_MESSAGE = "This is to confirm Silent Eight has received";
  public static final int WAIT_REGISTRATION_MAX_SECONDS = 20;

  private static GreenMail greenMail;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  CreateAlertMessageUseCase createAlertMessageUseCase;
  @Autowired
  ResourceLoader resourceLoader;
  @Autowired
  PaymentsBridgeEventsListener paymentsBridgeEventsListener;
  @Autowired
  MockAlertUseCase mockAlertUseCase;
  @Autowired
  MockDatasourceService mockDatasourceService;
  @Autowired
  MockWarehouseService mockWarehouseService;
  @Autowired
  JdbcTemplate jdbcTemplate;

  @MockBean
  private IdGenerator idGenerator;

  private static final String ALERT_MESSAGE_ID = "cdd9c5ce-89dd-a37c-1e5f-b59b1f87f42b";

  @BeforeAll
  static void beforeAll() {
    greenMail = new GreenMail(getServerStartup(3025));
    greenMail.withConfiguration(GreenMailConfiguration
            .aConfig()
            .withUser("user", "password"))
        .start();

    mockAlertMessageIdsToLearningAlerts(databaseId++, ALERT_MESSAGE_ID);
  }

  private static void mockAlertMessageIdsToLearningAlerts(long databseId, String messageId) {
    var alert = Alert.newBuilder()
        .setAlertId(messageId)
        .setName("alerts/" + databseId).build();
    MockAlertUseCase.cacheAlert(databseId, alert);
  }

  private static void assertMailSent() {
    await().atMost(4, SECONDS).untilAsserted(() -> {
      MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
      assertThat(receivedMessages.length).isGreaterThanOrEqualTo(1);
      MimeMessage receivedMessage = receivedMessages[0];
      assertEquals(1, receivedMessage.getAllRecipients().length);
      assertEquals(EMAIL_RECEIVER, receivedMessage.getAllRecipients()[0].toString());
      var receivedMessageContent =
          new String(receivedMessage.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
      assertTrue(receivedMessageContent.contains(EMAIL_MESSAGE));
      assertTrue(receivedMessage.getSubject().contains(EMAIL_SUBJECT));
    });
  }

  static Stream<String> filesFactory() {
    return VALID_REQUEST_FILES.stream();
  }

  @AfterAll
  static void afterAll() {
    greenMail.stop();
  }

  @ParameterizedTest
  @MethodSource("filesFactory")
  void shouldRegisterAlertAndInputs(String fileName) {
    // assumed h_r_gtex is same format as gtex message
    var alertId = createAlert(fileName);
    await()
        .conditionEvaluationListener(new ConditionEvaluationLogger(log::info))
        .atMost(Duration.ofSeconds(WAIT_REGISTRATION_MAX_SECONDS))
        .until(() -> paymentsBridgeEventsListener.containsRegisteredAlert(alertId));
  }


  @Test
  void shouldRemoveAlertRetention() {
    var alertId = createAlert("test_retention.json");
    await()
        .conditionEvaluationListener(new ConditionEvaluationLogger(log::info))
        .atMost(Duration.ofSeconds(10))
        .until(() -> paymentsBridgeEventsListener.containsRegisteredAlert(alertId));

    await()
        .conditionEvaluationListener(new ConditionEvaluationLogger(log::info))
        .atMost(Duration.ofSeconds(10))
        .until(() -> isAlertRemoved(alertId));

    assertThat(jdbcTemplate.queryForObject(
        String.format(
            "SELECT count(*) from pb_alert_message_payload WHERE alert_message_id = '%s'", alertId),
        Integer.class)).isEqualTo(0);

    assertThat(jdbcTemplate.queryForObject(
        String.format(
            "SELECT count(*) from pb_alert_message_status WHERE alert_message_id = '%s'", alertId),
        Integer.class)).isEqualTo(0);

    assertThat(jdbcTemplate.queryForObject(
        String.format("SELECT count(*) from pb_recommendation WHERE alert_id = '%s'", alertId),
        Integer.class)).isEqualTo(0);
  }

  private Boolean isAlertRemoved(UUID alertId) {
    return jdbcTemplate.queryForObject(
        String.format(
            "SELECT count(*) from pb_alert_message WHERE alert_message_id = '%s'", alertId),
        Integer.class) == 0;
  }

  @Test
  void shouldProcessLearningFilesUnregisteredAlert() {
    when(idGenerator.generateId())
        .thenReturn(UUID.fromString(ALERT_MESSAGE_ID));
    assertAlertRegistered();
    assertAlertIndexed(ALERT_MESSAGE_ID);
    var alertName = MockAlertUseCase.getAlertName(ALERT_MESSAGE_ID);
    assertUdsValuesCreated(alertName);
    assertMailSent();
  }

  private void assertUdsValuesCreated(String alertName) {
    assertThat(mockDatasourceService.getCreatedFeatureInputsCount(alertName)).isEqualTo(8);
    assertThat(mockDatasourceService.getCreatedCategoryValuesCount(alertName)).isEqualTo(8);
  }

  private void assertAlertIndexed(String discriminator) {
    await()
        .atMost(Duration.ofSeconds(WAIT_REGISTRATION_MAX_SECONDS))
        .until(() -> mockWarehouseService.containsIndexedDiscriminator(discriminator));
  }

  private void assertAlertRegistered() {
    await()
        .atMost(Duration.ofSeconds(WAIT_REGISTRATION_MAX_SECONDS))
        .until(() -> paymentsBridgeEventsListener.containsLearningRegisteredSystemId(
            ALERT_MESSAGE_ID));
    assertTrue(MockAlertUseCase.containsAlertId(ALERT_MESSAGE_ID));
  }

  private UUID createAlert(String fileName) {
    var fircoAlertMessage = createFircoAlertMessage(readFile(fileName));
    createAlertMessageUseCase.createAlertMessage(fircoAlertMessage);
    return fircoAlertMessage.getId();
  }

  private FircoAlertMessage createFircoAlertMessage(File srcFile) {
    try {
      var requestDto = objectMapper.readValue(srcFile, RequestDto.class);
      var dto = requestDto.getAlerts().get(0);
      var uuid = UUID.randomUUID();
      mockAlertMessageIdsToLearningAlerts(databaseId++, uuid.toString());
      return new FircoAlertMessage(uuid, OffsetDateTime.now(Clock.systemUTC()), dto,
          "", "", "login", "password");
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private File readFile(String fileName) {
    try {
      return resourceLoader.getResource(
          "classpath:" + SAMPLE_REQUESTS_DIR + "/" + fileName).getFile();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Test
  void shouldRejectAlertWithTooManyHits() {
    var alertId = createAlert(TOO_MANY_HITS_REQUEST_FILE);
    await()
        .conditionEvaluationListener(new ConditionEvaluationLogger(log::info))
        .atMost(Duration.ofSeconds(3))
        .until(() -> !paymentsBridgeEventsListener.containsRegisteredAlert(alertId));
  }
}
