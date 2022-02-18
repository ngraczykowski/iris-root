package com.silenteight.payments.bridge.app.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.PaymentsBridgeApplication;
import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.alertmessage.port.CreateAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.dto.input.RequestDto;
import com.silenteight.payments.bridge.mock.ae.MockAlertUseCase;
import com.silenteight.payments.bridge.mock.datasource.MockDatasourceService;
import com.silenteight.payments.bridge.mock.warehouse.MockWarehouseService;
import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.awaitility.core.ConditionEvaluationLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import javax.mail.internet.MimeMessage;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = { PaymentsBridgeApplication.class })
@ContextConfiguration(initializers = { RabbitTestInitializer.class, PostgresTestInitializer.class })
@Slf4j
@ActiveProfiles({
    "mockae", "mockdatasource", "mockgovernance", "mockagents", "mockaws", "test",
    "mockwarehouse" })
class PaymentsBridgeApplicationIT {

  private static final String SAMPLE_REQUESTS_DIR = "requests";
  private static final List<String> VALID_REQUEST_FILES = List.of(
      "2021_10_08-1754_uat_firco_alert.json");

  private static final String TOO_MANY_HITS_REQUEST_FILE =
      "2021-10-01_1837_osama_bin_laden.json";

  @RegisterExtension
  static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
      .withConfiguration(GreenMailConfiguration
          .aConfig()
          .withUser("user", "password"))
      .withPerMethodLifecycle(false);

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  CreateAlertMessageUseCase createAlertMessageUseCase;
  @Autowired
  ResourceLoader resourceLoader;
  @Autowired
  HandleLearningAlertsUseCase handleLearningDataUseCase;
  @Autowired
  PaymentsBridgeEventsListener paymentsBridgeEventsListener;
  @Autowired
  MockAlertUseCase mockAlertUseCase;
  @Autowired
  MockDatasourceService mockDatasourceService;
  @Autowired
  MockWarehouseService mockWarehouseService;

  @ParameterizedTest
  @MethodSource("filesFactory")
  void shouldRegisterAlertAndInputs(String fileName) {
    var alertId = createAlert(fileName);
    await()
        .conditionEvaluationListener(new ConditionEvaluationLogger(log::info))
        .atMost(Duration.ofSeconds(10))
        .until(() -> paymentsBridgeEventsListener.containsRegisteredAlert(alertId));
  }

  @Test
  void shouldProcessLearningFilesUnregisteredAlert() {
    assertAlertRegistered();
    assertAlertIndexed("alert_system_id|87AB4899-BE5B-5E4F-E053-150A6C0A7A84");
    var alertName = MockAlertUseCase.getAlertName("alert_system_id");
    assertUdsValuesCreated(alertName);
    assertMailSent();

    assertAlertIndexed("system_id_2|87AB4899-BE5B-5E4F-E053-150A6C0A7A84");
  }

  @Test
  @Sql(scripts = "shouldProcessLearningFileWithRegisteredAlert.sql")
  void shouldProcessLearningFilesRegisteredAlert() {
    assertAlertIndexed("system_id_2|87AB4899-BE5B-5E4F-E053-150A6C0A7A84");
    assertMailSent();
  }

  private static void assertMailSent() {
    await().atMost(2, SECONDS).untilAsserted(() -> {
      MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
      assertThat(receivedMessages.length).isGreaterThanOrEqualTo(2);
      MimeMessage receivedMessage = receivedMessages[0];
      assertEquals(1, receivedMessage.getAllRecipients().length);
    });
  }

  private void assertUdsValuesCreated(String alertName) {
    assertThat(mockDatasourceService.getCreatedFeatureInputsCount(alertName)).isEqualTo(10);
    assertThat(mockDatasourceService.getCreatedCategoryValuesCount(alertName)).isEqualTo(8);
  }

  private void assertAlertIndexed(String discriminator) {
    await()
        .atMost(Duration.ofSeconds(20))
        .until(() -> mockWarehouseService.containsIndexedDiscriminator(discriminator));
  }

  private void assertAlertRegistered() {
    await()
        .atMost(Duration.ofSeconds(20))
        .until(() -> paymentsBridgeEventsListener.containsLearningRegisteredSystemId(
            "alert_system_id"));
    assertTrue(MockAlertUseCase.containsAlertId("alert_system_id"));
  }

  static Stream<String> filesFactory() {
    return VALID_REQUEST_FILES.stream();
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
      return new FircoAlertMessage(
          UUID.randomUUID(), OffsetDateTime.now(Clock.systemUTC()), dto,
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
