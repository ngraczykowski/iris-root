package com.silenteight.payments.bridge.app;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.input.RequestDto;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.*;
import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.alertmessage.port.CreateAlertMessageUseCase;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.awaitility.core.ConditionEvaluationLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;
import org.springframework.core.io.ResourceLoader;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = PaymentsBridgeApplication.class)
@ContextConfiguration(initializers = { RabbitTestInitializer.class, PostgresTestInitializer.class })
@Slf4j
@ActiveProfiles({"mockae", "mockdatasource", "mockgovernance", "test"})
class PaymentsBridgeApplicationTests {

  private static final String SAMPLE_REQUESTS_DIR = "requests";
  private static final List<String> VALID_REQUEST_FILES = List.of(
      "2021_10_08-1754_uat_firco_alert.json");

  private static final String TOO_MANY_HITS_REQUEST_FILE =
      "2021-10-01_1837_osama_bin_laden.json";

  @Autowired CommonChannels channels;
  @Autowired ObjectMapper objectMapper;
  @Autowired CreateAlertMessageUseCase createAlertMessageUseCase;
  @Autowired ResourceLoader resourceLoader;

  @ParameterizedTest
  @MethodSource("filesFactory")
  public void shouldRegisterAlertAndInputs(String fileName) {
    var eventRecorder = createRegistrationEventRecorder();
    createAlert(fileName);
    await()
        .conditionEvaluationListener(new ConditionEvaluationLogger(log::info))
        .atMost(Duration.ofSeconds(10))
        .until(eventRecorder::allCaught);
    eventRecorder.unsubscribeAll();
  }

  static Stream<String> filesFactory() {
    return VALID_REQUEST_FILES.stream();
  }

  private DomainEventRecorder createRegistrationEventRecorder() {
    var recorder = new DomainEventRecorder();
    recorder.subscribe(AlertStoredEvent.class, channels.alertStored());
    recorder.subscribe(AlertInitializedEvent.class, channels.alertInitialized());
    recorder.subscribe(AlertRegisteredEvent.class, channels.alertRegistered());
    recorder.subscribe(AlertInputAcceptedEvent.class, channels.alertInputAccepted());
    recorder.subscribe(RecommendationGeneratedEvent.class, channels.recommendationGenerated());
    recorder.subscribe(RecommendationCompletedEvent.class, channels.recommendationCompleted());
    return recorder;
  }

  private void createAlert(String fileName) {
    var fircoAlertMessage = createFircoAlertMessage(readFile(fileName));
    createAlertMessageUseCase.createAlertMessage(fircoAlertMessage);
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
  public void shouldRejectAlertWithTooManyHits() {
    var eventRecorder = createTooManyHitsEventRecorder();
    createAlert(TOO_MANY_HITS_REQUEST_FILE);
    await()
        .conditionEvaluationListener(new ConditionEvaluationLogger(log::info))
        .atMost(Duration.ofSeconds(10))
        .until(eventRecorder::allCaught);
    eventRecorder.unsubscribeAll();
  }

  private DomainEventRecorder createTooManyHitsEventRecorder() {
    var recorder = new DomainEventRecorder();
    recorder.subscribe(AlertStoredEvent.class, channels.alertStored());
    recorder.subscribe(RecommendationCompletedEvent.class, channels.recommendationCompleted());
    return recorder;
  }

  private static class DomainEventRecorder implements MessageHandler, Ordered {

    private final Map<Class<? extends DomainEvent>, SubscribableChannel> recordingEvents =
        new HashMap<>();
    private final Set<Class<? extends DomainEvent>> caughtEvents = new HashSet<>();

    public DomainEventRecorder subscribe(
        Class<? extends DomainEvent> domainEventClass, SubscribableChannel subscribableChannel) {
      recordingEvents.put(domainEventClass, subscribableChannel);
      subscribableChannel.subscribe(this);
      return this;
    }

    public void unsubscribeAll() {
      recordingEvents.forEach((key, value) -> value.unsubscribe(this));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
      if (recordingEvents.containsKey(message.getPayload().getClass())) {
        log.info("Caught domain-event: {}", message.getPayload().getClass());
        caughtEvents.add((Class<? extends DomainEvent>) message.getPayload().getClass());
      }
    }

    public boolean allCaught() {
      return recordingEvents.size() == caughtEvents.size();
    }

    @Override
    public int getOrder() {
      return 1;
    }
  }

}
