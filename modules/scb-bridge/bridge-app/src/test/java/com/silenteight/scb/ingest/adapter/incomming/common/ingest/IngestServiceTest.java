package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.model.RegistrationAlertContext;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.sep.base.testing.messaging.MessageSenderSpyFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.scb.ingest.domain.model.AlertSource.CBS;
import static com.silenteight.scb.ingest.domain.model.Batch.Priority.MEDIUM;
import static java.util.Collections.singleton;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestServiceTest {

  private static final String DECISION_GROUP = "decision_group";
  private static final String OTHER_DECISION_GROUP = "other_decision_group";

  @Mock
  private IngestServiceListener listener;
  @Mock
  private ScbRecommendationService scbRecommendationService;
  @Mock
  private AlertRegistrationFacade alertRegistrationFacade;
  @Mock
  private IngestEventPublisher ingestEventPublisher;

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
    var internalBatchId = InternalBatchIdGenerator.generate();
    var alerts = createAlerts();
    when(scbRecommendationService.alertRecommendationExists(anyString(), anyString()))
        .thenReturn(true);
    when(alertRegistrationFacade.registerLearningAlert(any(), any()))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForLearn(internalBatchId, alerts);

    //then
    verify(alertRegistrationFacade).registerLearningAlert(any(), any());
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  private static List<Alert> createAlerts() {
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
                .build())
        .toList();
  }

  @Test
  void ingestAlertsWithoutExistingRecommendationForLearn() {
    //given
    var internalBatchId = InternalBatchIdGenerator.generate();
    var alerts = createAlerts();
    when(scbRecommendationService.alertRecommendationExists(anyString(), anyString()))
        .thenReturn(false);
    when(alertRegistrationFacade.registerLearningAlert(any(), any()))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForLearn(internalBatchId, alerts);

    //then
    verify(alertRegistrationFacade).registerLearningAlert(any(), any());
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  @Test
  void ingestAlertsForRecommendation() {
    //given
    var alerts = createAlerts();
    var internalBatchId = InternalBatchIdGenerator.generate();
    var alertContext = new RegistrationAlertContext(MEDIUM, CBS);
    when(alertRegistrationFacade.registerSolvingAlert(internalBatchId, alerts, alertContext))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, alertContext);

    //then
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  @Test
  void ingestDenyAlertsForRecommendation() {
    //given
    var internalBatchId = InternalBatchIdGenerator.generate();
    var denyAlerts = createDenyAlerts().toList();
    var alertContext = new RegistrationAlertContext(MEDIUM, CBS);
    when(alertRegistrationFacade.registerSolvingAlert(internalBatchId, denyAlerts, alertContext))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, denyAlerts, alertContext);

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
    var internalBatchId = InternalBatchIdGenerator.generate();
    var nonDenyAlerts = createNonDenyAlerts().toList();
    var alertContext = new RegistrationAlertContext(MEDIUM, CBS);
    when(alertRegistrationFacade.registerSolvingAlert(internalBatchId, nonDenyAlerts, alertContext))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, nonDenyAlerts, alertContext);

    //then
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  private static Stream<Alert> createNonDenyAlerts() {
    return createSolvingAlerts("S8_TEST");
  }

  @Test
  void shouldIngestSolvedAlertsForRecommendationWhenBacktestReportModeIsEnabled() {
    //given
    var internalBatchId = InternalBatchIdGenerator.generate();
    ingestProperties.setSolvedAlertsProcessingEnabled(true);
    createIngestService();
    var obsoleteMatch = Match.builder().flags(Match.Flag.OBSOLETE.getValue()).build();
    var solvedMatch = Match.builder().flags(Match.Flag.SOLVED.getValue()).build();
    var objectId = ObjectId.builder().sourceId("").discriminator("").build();
    var alerts = Stream.of(
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
    var alertContext = new RegistrationAlertContext(MEDIUM, CBS);
    when(alertRegistrationFacade.registerSolvingAlert(internalBatchId, alerts, alertContext))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, alertContext);

    //then
    verify(ingestEventPublisher, times(2)).publish(any());
  }
}
