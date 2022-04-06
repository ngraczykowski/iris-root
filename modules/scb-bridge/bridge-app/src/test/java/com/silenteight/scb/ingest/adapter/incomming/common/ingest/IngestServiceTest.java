package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator;
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.scb.ingest.domain.model.Batch.Priority.MEDIUM;
import static com.silenteight.scb.ingest.domain.model.BatchSource.CBS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestServiceTest {

  private static final String DECISION_GROUP = "decision_group";
  private static final String OTHER_DECISION_GROUP = "other_decision_group";

  @Mock
  private ScbRecommendationService scbRecommendationService;
  @Mock
  private AlertRegistrationFacade alertRegistrationFacade;
  @Mock
  private IngestEventPublisher ingestEventPublisher;

  private IngestService ingestService;

  @BeforeEach
  void setUp() {
    createIngestService();
  }

  private void createIngestService() {
    IngestConfiguration configuration =
        new IngestConfiguration(alertRegistrationFacade, ingestEventPublisher);
    ingestService = configuration.ingestService(
        scbRecommendationService);
  }

  @Test
  void ingestAlertsWithExistingRecommendationForLearn() {
    //given
    var internalBatchId = InternalBatchIdGenerator.generate();
    var alerts = createAlerts();
    when(scbRecommendationService.alertRecommendationExists(anyString(), anyString()))
        .thenReturn(true);
    when(alertRegistrationFacade.registerLearningAlerts(any(), any()))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForLearn(internalBatchId, alerts);

    //then
    verify(alertRegistrationFacade).registerLearningAlerts(any(), any());
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
                .details(AlertDetails.builder().systemId("system-id").batchId("batchId1").build())
                .build(),
            Alert
                .builder()
                .id(objectId)
                .matches(Collections.singletonList(someMatch))
                .decisionGroup(OTHER_DECISION_GROUP)
                .details(AlertDetails.builder().systemId("system-id").batchId("batchId1").build())
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
    when(alertRegistrationFacade.registerLearningAlerts(any(), any()))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForLearn(internalBatchId, alerts);

    //then
    verify(alertRegistrationFacade).registerLearningAlerts(any(), any());
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  @Test
  void ingestAlertsForRecommendation() {
    //given
    var alerts = createAlerts();
    var internalBatchId = InternalBatchIdGenerator.generate();
    var batchContext = new RegistrationBatchContext(MEDIUM, CBS);
    when(alertRegistrationFacade.registerSolvingAlerts(internalBatchId, alerts, batchContext))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, batchContext);

    //then
    verify(ingestEventPublisher, times(2)).publish(any());
  }

  @Test
  void ingestDenyAlertsForRecommendation() {
    //given
    var internalBatchId = InternalBatchIdGenerator.generate();
    var denyAlerts = createDenyAlerts().toList();
    var batchContext = new RegistrationBatchContext(MEDIUM, CBS);
    when(alertRegistrationFacade.registerSolvingAlerts(internalBatchId, denyAlerts, batchContext))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, denyAlerts, batchContext);

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
    var batchContext = new RegistrationBatchContext(MEDIUM, CBS);
    when(
        alertRegistrationFacade.registerSolvingAlerts(internalBatchId, nonDenyAlerts, batchContext))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, nonDenyAlerts, batchContext);

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
    var batchContext = new RegistrationBatchContext(MEDIUM, CBS);
    when(alertRegistrationFacade.registerSolvingAlerts(internalBatchId, alerts, batchContext))
        .thenReturn(RegistrationResponse.builder().build());

    //when
    ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, batchContext);

    //then
    verify(ingestEventPublisher, times(2)).publish(any());
  }
}
