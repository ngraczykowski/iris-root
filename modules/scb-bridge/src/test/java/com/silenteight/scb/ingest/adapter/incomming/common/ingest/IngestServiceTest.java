package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.alert.Match;
import com.silenteight.proto.serp.v1.alert.Match.Flags;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.sep.base.common.messaging.properties.CorrelatedMessagePropertiesProvider;
import com.silenteight.sep.base.testing.messaging.MessageSenderSpy;
import com.silenteight.sep.base.testing.messaging.MessageSenderSpyFactory;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;
import java.util.stream.Stream;

import static com.silenteight.proto.serp.v1.alert.Alert.Flags.FLAG_ATTACH_VALUE;
import static com.silenteight.proto.serp.v1.alert.Alert.Flags.FLAG_LEARN_VALUE;
import static com.silenteight.proto.serp.v1.alert.Alert.Flags.FLAG_PROCESS_VALUE;
import static com.silenteight.proto.serp.v1.alert.Alert.Flags.FLAG_RECOMMEND_VALUE;
import static com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants.ALERT_DENY_PRIORITY;
import static com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants.ALERT_NON_DENY_PRIORITY;
import static com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants.HEADER_PRIORITY;
import static java.util.Collections.singleton;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.amqp.support.AmqpHeaders.CORRELATION_ID;

@ExtendWith(MockitoExtension.class)
class IngestServiceTest {

  private static final int PRIORITY_VALUE = 10;
  private static final String DECISION_GROUP = "decision_group";
  private static final String OTHER_DECISION_GROUP = "other_decision_group";
  private static final String CORRELATION_ID_VALUE = randomUUID().toString();

  @Mock
  private IngestServiceListener listener;
  @Mock
  private ScbRecommendationService scbRecommendationService;
  @Captor
  private ArgumentCaptor<Alert> alertCaptor;

  private MessageSenderSpyFactory senderFactory;

  private IngestProperties ingestProperties;
  private IngestService ingestService;

  @BeforeEach
  void setUp() {
    ingestProperties = new IngestProperties();
    senderFactory = new MessageSenderSpyFactory();
    createIngestService();
  }

  @Test
  void ingestAlertForRecommend() {
    //given
    Alert alert = Alert.newBuilder().setDecisionGroup(DECISION_GROUP).build();
    MessageHeaders messageHeaders =
        new MessageHeaders(Map.of(
            CORRELATION_ID, CORRELATION_ID_VALUE,
            HEADER_PRIORITY, PRIORITY_VALUE));
    CorrelatedMessagePropertiesProvider propertiesProvider =
        new CorrelatedMessagePropertiesProvider(messageHeaders);

    //when
    ingestService.ingestOrderedAlert(alert, propertiesProvider);

    //then
    assertThat(senderFactory.getLastExchangeName()).isEqualTo(ingestProperties.getOutputExchange());
    assertThat(senderFactory.getLastMessageSender())
        .extracting(MessageSenderSpy::getMessagePropertiesReceived)
        .satisfies(IngestServiceTest::assertProperties);
    verify(listener).send(alertCaptor.capture());
    assertThat(alertCaptor.getValue().getDecisionGroup()).isEqualTo(DECISION_GROUP);
    assertThat(alertCaptor.getValue().getFlags())
        .isEqualTo(FLAG_PROCESS_VALUE | FLAG_RECOMMEND_VALUE | FLAG_ATTACH_VALUE);
    assertThat(alertCaptor.getValue().getIngestedAt()).isNotNull();
  }

  @Test
  void ingestAlertsWithExistingRecommendationForLearn() {
    //given
    Stream<Alert> alerts = createAlerts();
    when(scbRecommendationService.alertRecommendationExists(anyString(), anyString()))
        .thenReturn(true);

    //when
    ingestService.ingestAlertsForLearn(alerts);

    //then
    checkAndAssertResults(FLAG_LEARN_VALUE);
    assertThat(senderFactory.getLastMessageSender())
        .extracting(MessageSenderSpy::getMessagePropertiesReceived)
        .satisfies(properties -> assertThat(properties).isNull());
  }

  @Test
  void ingestAlertsWithoutExistingRecommendationForLearn() {
    //given
    Stream<Alert> alerts = createAlerts();
    when(scbRecommendationService.alertRecommendationExists(anyString(), anyString()))
        .thenReturn(false);

    //when
    ingestService.ingestAlertsForLearn(alerts);

    //then
    checkAndAssertResults(FLAG_LEARN_VALUE | FLAG_PROCESS_VALUE);
    assertThat(senderFactory.getLastMessageSender())
        .extracting(MessageSenderSpy::getMessagePropertiesReceived)
        .satisfies(properties -> assertThat(properties).isNull());
  }

  @Test
  void ingestAlertsForRecommendation() {
    //given
    Stream<Alert> alerts = createAlerts();

    //when
    ingestService.ingestAlertsForRecommendation(alerts);

    //then
    checkAndAssertResults(FLAG_RECOMMEND_VALUE | FLAG_PROCESS_VALUE | FLAG_ATTACH_VALUE);
  }

  @Test
  void ingestDenyAlertsForRecommendation() {
    //given
    Stream<Alert> denyAlerts = createDenyAlerts();

    //when
    ingestService.ingestAlertsForRecommendation(denyAlerts);

    //then
    checkAndAssertResults(FLAG_RECOMMEND_VALUE | FLAG_PROCESS_VALUE | FLAG_ATTACH_VALUE);
    assertThat(senderFactory.getLastMessageSender())
        .extracting(MessageSenderSpy::getMessagePropertiesReceived)
        .satisfies(properties -> {
          MessageProperties messageProperties = properties;
          assertThat(messageProperties.getCorrelationId()).isNull();
          assertThat(messageProperties.getPriority()).isEqualTo(ALERT_DENY_PRIORITY);
        });
  }

  @Test
  void ingestNonDenyAlertsForRecommendation() {
    //given
    Stream<Alert> nonDenyAlerts = createNonDenyAlerts();

    //when
    ingestService.ingestAlertsForRecommendation(nonDenyAlerts);

    //then
    checkAndAssertResults(FLAG_RECOMMEND_VALUE | FLAG_PROCESS_VALUE | FLAG_ATTACH_VALUE);
    assertThat(senderFactory.getLastMessageSender())
        .extracting(MessageSenderSpy::getMessagePropertiesReceived)
        .satisfies(properties -> {
          MessageProperties messageProperties = properties;
          assertThat(messageProperties.getCorrelationId()).isNull();
          assertThat(messageProperties.getPriority()).isEqualTo(ALERT_NON_DENY_PRIORITY);
        });
  }

  @Test
  void shouldDoNotIngestSolvedAlertsForRecommendation() {
    //given
    Match obsoleteMatch = Match.newBuilder().setFlags(Flags.FLAG_OBSOLETE_VALUE).build();
    Match solvedMatch = Match.newBuilder().setFlags(Flags.FLAG_SOLVED_VALUE).build();

    Stream<Alert> alerts = Stream.of(
        Alert.newBuilder().addMatches(obsoleteMatch).setDecisionGroup(DECISION_GROUP).build(),
        Alert.newBuilder().addMatches(solvedMatch).setDecisionGroup(OTHER_DECISION_GROUP).build());

    //when
    ingestService.ingestAlertsForRecommendation(alerts);

    //then
    assert senderFactory.getLastMessageSender().getSentMessage() == null;
    verify(listener, never()).send(any());
  }

  @Test
  void shouldIngestSolvedAlertsForRecommendationWhenBacktestReportModeIsEnabled() {
    //given
    ingestProperties.setSolvedAlertsProcessingEnabled(true);
    createIngestService();
    Match obsoleteMatch = Match.newBuilder().setFlags(Flags.FLAG_OBSOLETE_VALUE).build();
    Match solvedMatch = Match.newBuilder().setFlags(Flags.FLAG_SOLVED_VALUE).build();

    Stream<Alert> alerts = Stream.of(
        Alert.newBuilder().addMatches(obsoleteMatch).setDecisionGroup(DECISION_GROUP).build(),
        Alert.newBuilder().addMatches(solvedMatch).setDecisionGroup(OTHER_DECISION_GROUP).build());

    //when
    ingestService.ingestAlertsForRecommendation(alerts);

    //then
    checkAndAssertResults(FLAG_RECOMMEND_VALUE | FLAG_PROCESS_VALUE | FLAG_ATTACH_VALUE);
  }

  private void createIngestService() {
    IngestConfiguration configuration = new IngestConfiguration(ingestProperties);
    ingestService = configuration.ingestService(
        senderFactory,
        singleton(listener),
        scbRecommendationService);
  }

  private void checkAndAssertResults(int learnFlags) {
    assertThat(senderFactory.getLastExchangeName()).isEqualTo(ingestProperties.getOutputExchange());

    assertThat(getLastSentFlags()).isEqualTo(learnFlags);

    verify(listener, times(2)).send(any());
  }

  private static Stream<Alert> createDenyAlerts() {
    return createSolvingAlerts("DENY_TEST");
  }

  private static Stream<Alert> createNonDenyAlerts() {
    return createSolvingAlerts("S8_TEST");
  }

  private static Stream<Alert> createSolvingAlerts(String unit) {
    Match someMatch = Match.newBuilder().build();
    Any details = Any.pack(ScbAlertDetails.newBuilder().setUnit(unit).build());
    return Stream.of(
        Alert.newBuilder()
            .addMatches(someMatch)
            .setDecisionGroup(DECISION_GROUP)
            .setDetails(details)
            .build(),
        Alert.newBuilder()
            .addMatches(someMatch)
            .setDecisionGroup(OTHER_DECISION_GROUP)
            .setDetails(details)
            .build());
  }

  private static Stream<Alert> createAlerts() {
    Match someMatch = Match.newBuilder().build();
    return Stream.of(
        Alert.newBuilder().addMatches(someMatch).setDecisionGroup(DECISION_GROUP).build(),
        Alert.newBuilder().addMatches(someMatch).setDecisionGroup(OTHER_DECISION_GROUP).build());
  }

  private static void assertProperties(MessageProperties properties) {
    assertThat(properties.getCorrelationId()).isEqualTo(CORRELATION_ID_VALUE);
    assertThat(properties.getPriority()).isEqualTo(PRIORITY_VALUE);
  }

  private int getLastSentFlags() {
    Message sentMessage = senderFactory.getLastMessageSender().getSentMessage();
    assert sentMessage != null;
    assert sentMessage instanceof Alert;
    return ((Alert) sentMessage).getFlags();
  }
}
