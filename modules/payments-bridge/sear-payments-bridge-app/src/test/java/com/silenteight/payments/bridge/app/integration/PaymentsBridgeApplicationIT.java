package com.silenteight.payments.bridge.app.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.PaymentsBridgeApplication;
import com.silenteight.payments.bridge.common.dto.input.RequestDto;
import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.alertmessage.port.CreateAlertMessageUseCase;
import com.silenteight.payments.bridge.mock.ae.MockAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.awaitility.core.ConditionEvaluationLogger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = { PaymentsBridgeApplication.class })
@ContextConfiguration(initializers = { RabbitTestInitializer.class, PostgresTestInitializer.class })
@Slf4j
@ActiveProfiles({ "mockae", "mockdatasource", "mockgovernance", "mockagents", "mockaws", "test" })
class PaymentsBridgeApplicationIT {

  private static final String SAMPLE_REQUESTS_DIR = "requests";
  private static final List<String> VALID_REQUEST_FILES = List.of(
      "2021_10_08-1754_uat_firco_alert.json");

  private static final String TOO_MANY_HITS_REQUEST_FILE =
      "2021-10-01_1837_osama_bin_laden.json";

  @Autowired ObjectMapper objectMapper;
  @Autowired CreateAlertMessageUseCase createAlertMessageUseCase;
  @Autowired ResourceLoader resourceLoader;
  @Autowired HandleLearningAlertsUseCase handleLearningDataUseCase;
  @Autowired PaymentsBridgeEventsListener paymentsBridgeEventsListener;

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
  @Disabled
  void shouldProcessLearningCsv() {
    var request =
        LearningRequest.builder().bucket("bucket").object("analystdecison-2-hits.csv").build();
    handleLearningDataUseCase.readAlerts(request);
    await()
        .conditionEvaluationListener(new ConditionEvaluationLogger(log::info))
        .atMost(Duration.ofSeconds(1))
        .until(PaymentsBridgeApplicationIT::createdAlerts);
    assertThat(MockAlertUseCase.getCreatedAlertsCount()).isEqualTo(2);
    assertThat(MockAlertUseCase.getCreatedMatchesCount()).isEqualTo(2);
  }

  public static boolean createdAlerts() {
    return MockAlertUseCase.getCreatedAlertsCount() >= 2;
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

