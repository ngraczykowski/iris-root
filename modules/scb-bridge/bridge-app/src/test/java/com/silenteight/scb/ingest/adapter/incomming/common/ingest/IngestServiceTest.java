package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.model.Batch.Priority;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.sep.base.testing.messaging.MessageSenderSpyFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

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
  @Mock
  private AlertRegistrationFacade alertRegistrationFacade;
  @Mock
  private IngestEventPublisher ingestEventPublisher;
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

  private void createIngestService() {
    IngestConfiguration configuration =
        new IngestConfiguration(ingestProperties, alertRegistrationFacade, ingestEventPublisher);
    ingestService = configuration.ingestService(
        senderFactory,
        singleton(listener),
        scbRecommendationService);
  }

  @Test
  void ingestAlertsWithExistingRecommendationForLearn() {
    //given
    Stream<Alert> alerts = createAlerts();
    when(scbRecommendationService.alertRecommendationExists(anyString(), anyString()))
        .thenReturn(true);
    when(alertRegistrationFacade.registerLearningAlert(any(), any()))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForLearn(alerts);

    //then
    verify(alertRegistrationFacade).registerLearningAlert(any(), any());
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  private static Stream<Alert> createAlerts() {
    Match someMatch = Match.builder().build();
    ObjectId objectId = ObjectId.builder().sourceId("").discriminator("").build();
    return Stream.of(
        Alert
            .builder()
            .id(objectId)
            .matches(Collections.singletonList(someMatch))
            .decisionGroup(DECISION_GROUP)
            .details(AlertDetails.builder().batchId("batchId1").build())
            .build(),
        Alert
            .builder()
            .id(objectId)
            .matches(Collections.singletonList(someMatch))
            .decisionGroup(OTHER_DECISION_GROUP)
            .details(AlertDetails.builder().batchId("batchId1").build())
            .build());
  }

  @Test
  void ingestAlertsWithoutExistingRecommendationForLearn() {
    //given
    Stream<Alert> alerts = createAlerts();
    when(scbRecommendationService.alertRecommendationExists(anyString(), anyString()))
        .thenReturn(false);
    when(alertRegistrationFacade.registerLearningAlert(any(), any()))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForLearn(alerts);

    //then
    verify(alertRegistrationFacade).registerLearningAlert(any(), any());
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  @Test
  void ingestAlertsForRecommendation() {
    //given
    List<Alert> alerts = createAlerts().toList();
    String internalBatchId = UUID.randomUUID().toString();
    when(alertRegistrationFacade.registerSolvingAlert(internalBatchId, alerts, Priority.MEDIUM))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, alerts);

    //then
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  @Test
  void ingestDenyAlertsForRecommendation() {
    //given
    String internalBatchId = UUID.randomUUID().toString();
    List<Alert> denyAlerts = createDenyAlerts().toList();
    when(alertRegistrationFacade.registerSolvingAlert(internalBatchId, denyAlerts, Priority.MEDIUM))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, denyAlerts);

    //then
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  private static Stream<Alert> createDenyAlerts() {
    return createSolvingAlerts("DENY_TEST");
  }

  private static Stream<Alert> createSolvingAlerts(String unit) {
    Match someMatch = Match.builder().build();
    AlertDetails details = AlertDetails.builder().batchId("batchId1").build();
    ObjectId objectId = ObjectId.builder().sourceId("").discriminator("").build();
    return Stream.of(
        Alert.builder()
            .id(objectId)
            .matches(Collections.singletonList(someMatch))
            .decisionGroup(DECISION_GROUP)
            .details(details)
            .build(),
        Alert.builder()
            .id(objectId)
            .matches(Collections.singletonList(someMatch))
            .decisionGroup(OTHER_DECISION_GROUP)
            .details(details)
            .build());
  }

  @Test
  void ingestNonDenyAlertsForRecommendation() {
    //given
    String internalBatchId = UUID.randomUUID().toString();
    List<Alert> nonDenyAlerts = createNonDenyAlerts().toList();
    when(alertRegistrationFacade.registerSolvingAlert(internalBatchId, nonDenyAlerts,
        Priority.MEDIUM)).thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, nonDenyAlerts);

    //then
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  private static Stream<Alert> createNonDenyAlerts() {
    return createSolvingAlerts("S8_TEST");
  }

  @Test
  void shouldIngestSolvedAlertsForRecommendationWhenBacktestReportModeIsEnabled() {
    //given
    String internalBatchId = UUID.randomUUID().toString();
    ingestProperties.setSolvedAlertsProcessingEnabled(true);
    createIngestService();
    Match obsoleteMatch = Match.builder().flags(Match.Flag.OBSOLETE.getValue()).build();
    Match solvedMatch = Match.builder().flags(Match.Flag.SOLVED.getValue()).build();
    ObjectId objectId = ObjectId.builder().sourceId("").discriminator("").build();
    List<Alert> alerts = Stream.of(
            Alert
                .builder()
                .id(objectId)
                .matches(Collections.singletonList(obsoleteMatch))
                .decisionGroup(DECISION_GROUP)
                .details(AlertDetails.builder().batchId("batchId1").build())
                .build(),
            Alert
                .builder()
                .id(objectId)
                .matches(Collections.singletonList(solvedMatch))
                .decisionGroup(OTHER_DECISION_GROUP)
                .details(AlertDetails.builder().batchId("batchId1").build())
                .build())
        .toList();
    when(alertRegistrationFacade.registerSolvingAlert(internalBatchId, alerts, Priority.MEDIUM))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, alerts);

    //then
    verify(ingestEventPublisher, times(2)).publish(any());
  }
}
