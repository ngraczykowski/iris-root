package com.silenteight.simulator.processing.alert.index.feed;

import com.silenteight.data.api.v2.SimulationAlert;
import com.silenteight.data.api.v2.SimulationDataIndexRequest;
import com.silenteight.data.api.v2.SimulationMatch;
import com.silenteight.simulator.processing.alert.index.amqp.gateway.SimulationDataIndexRequestGateway;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import com.google.protobuf.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.silenteight.simulator.processing.alert.index.fixtures.RecommendationFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationsGeneratedUseCaseTest {

  private static final int BATCH_SIZE = 5;

  private RecommendationsGeneratedUseCase underTest;
  @Mock
  private IndexedAlertService indexedAlertService;
  @Mock
  private RequestIdGenerator requestIdGenerator;
  @Mock
  private SimulationDataIndexRequestGateway gateway;

  @BeforeEach
  void setUp() {
    underTest = new RecommendationsGeneratedUseCase(
        indexedAlertService, requestIdGenerator, gateway, BATCH_SIZE);
  }

  @Test
  void shouldHandleMessageWithMultipleAlerts() {
    // given
    when(requestIdGenerator.generate()).thenReturn(REQUEST_ID);

    // when
    underTest.handle(MULTIPLE_ALERTS_REQUEST);

    // then
    ArgumentCaptor<String> requestId = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> analysisName = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Long> alertCount = ArgumentCaptor.forClass(Long.class);

    verify(indexedAlertService, times(2)).saveAsSent(
        requestId.capture(), analysisName.capture(), alertCount.capture());

    assertThat(requestId.getAllValues().get(0)).isEqualTo(REQUEST_ID);
    assertThat(analysisName.getAllValues().get(0)).isEqualTo(ANALYSIS_NAME);
    assertThat(alertCount.getAllValues().get(0)).isEqualTo(BATCH_SIZE);
    assertThat(requestId.getAllValues().get(1)).isEqualTo(REQUEST_ID);
    assertThat(analysisName.getAllValues().get(1)).isEqualTo(ANALYSIS_NAME);
    assertThat(alertCount.getAllValues().get(1)).isEqualTo(BATCH_SIZE);
    ArgumentCaptor<SimulationDataIndexRequest> indexRequest
        = ArgumentCaptor.forClass(SimulationDataIndexRequest.class);

    verify(gateway, times(2)).send(indexRequest.capture());
    assertThat(indexRequest.getAllValues().get(0).getRequestId()).isEqualTo(REQUEST_ID);
    assertThat(indexRequest.getAllValues().get(0).getAnalysisName()).isEqualTo(ANALYSIS_NAME);
    assertThat(indexRequest.getAllValues().get(0).getAlertsCount()).isEqualTo(BATCH_SIZE);
    assertThat(indexRequest.getAllValues().get(1).getRequestId()).isEqualTo(REQUEST_ID);
    assertThat(indexRequest.getAllValues().get(1).getAnalysisName()).isEqualTo(ANALYSIS_NAME);
    assertThat(indexRequest.getAllValues().get(1).getAlertsCount()).isEqualTo(BATCH_SIZE);
  }

  @Test
  void shouldHandleMessageWithSingleAlert() {
    // given
    when(requestIdGenerator.generate()).thenReturn(REQUEST_ID);

    // when
    underTest.handle(REQUEST);

    // then
    ArgumentCaptor<String> requestId = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> analysisName = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Long> alertCount = ArgumentCaptor.forClass(Long.class);

    verify(indexedAlertService, times(1)).saveAsSent(
        requestId.capture(), analysisName.capture(), alertCount.capture());
    assertThat(requestId.getAllValues().get(0)).isEqualTo(REQUEST_ID);
    assertThat(analysisName.getAllValues().get(0)).isEqualTo(ANALYSIS_NAME);
    assertThat(alertCount.getAllValues().get(0)).isEqualTo(1);
    ArgumentCaptor<SimulationDataIndexRequest> indexRequest
        = ArgumentCaptor.forClass(SimulationDataIndexRequest.class);

    verify(gateway, times(1)).send(indexRequest.capture());
    SimulationDataIndexRequest simulationDataIndexRequest = indexRequest.getAllValues().get(0);

    assertThat(simulationDataIndexRequest.getAnalysisName()).isEqualTo(ANALYSIS_NAME);
    assertThat(simulationDataIndexRequest.getAlertsList()).hasSize(1);
    assertThat(simulationDataIndexRequest.getRequestId()).isEqualTo(REQUEST_ID);
  }

  @Test
  void shouldHandleRequestWithMultiHitAlert() {
    //given
    when(requestIdGenerator.generate()).thenReturn(REQUEST_ID);

    //when
    underTest.handle(REQUEST_WITH_MULTI_HIT_ALERT);

    //then
    ArgumentCaptor<String> requestId = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> analysisName = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Long> alertCount = ArgumentCaptor.forClass(Long.class);

    verify(indexedAlertService, times(1)).saveAsSent(
        requestId.capture(), analysisName.capture(), alertCount.capture());
    assertThat(requestId.getAllValues().get(0)).isEqualTo(REQUEST_ID);
    assertThat(analysisName.getAllValues().get(0)).isEqualTo(ANALYSIS_NAME);
    assertThat(alertCount.getAllValues().get(0)).isEqualTo(1);
    ArgumentCaptor<SimulationDataIndexRequest> indexRequest
        = ArgumentCaptor.forClass(SimulationDataIndexRequest.class);

    verify(gateway, times(1)).send(indexRequest.capture());
    SimulationDataIndexRequest simulationDataIndexRequest = indexRequest.getAllValues().get(0);
    assertThat(simulationDataIndexRequest.getAlertsList()).hasSize(1);

    SimulationAlert alert = simulationDataIndexRequest.getAlerts(0);
    assertThat(alert.getName()).isEqualTo(ALERT_NAME);

    Map<String, Value> alertPayload = alert.getPayload().getFieldsMap();
    assertThat(alertPayload).hasSize(4);
    assertPayload(alertPayload, EXPECTED_ALERT_PAYLOAD);

    List<SimulationMatch> matchesList = alert.getMatchesList();
    assertThat(matchesList).hasSize(2);
    assertPayload(matchesList.get(1).getPayload().getFieldsMap(), EXPECTED_MATCH_PAYLOAD);
  }

  private void assertPayload(
      Map<String, Value> matchPayload, Map<String, String> expectedValues) {

    for (Map.Entry<String, String> entry : expectedValues.entrySet()) {
      assertThat(matchPayload).containsEntry(entry.getKey(), getStringAsValue(entry.getValue()));
    }
  }

  private static final Map<String, String> EXPECTED_ALERT_PAYLOAD =
      Map.of("recommendation_alert", ALERT_NAME,
          "recommendation_comment", RECOMMENDATION_COMMENT,
          "recommendation_recommended_action", RECOMMENDED_ACTION);

  private static final Map<String, String> EXPECTED_MATCH_PAYLOAD =
      Map.of("match_solution", MATCH_SOLUTION_TP,
          "match_comment", MATCH_COMMENT,
          "match_reason:step", "",
          "match_reason:policy", POLICY_NAME,
          "match_reason:step_title", " ",
          "match_reason:policy_title", POLICY_TITLE,
          "match_reason:feature_vector_signature", FEATURE_VECTOR_SIGNATURE,
          "features/name:config", AGENTS_CONFIG,
          "features/name:reason",
          "fields {\n  key: \"reason-1\"\n  "
              + "value {\n    string_value: \"This is reason\"\n  }\n}\n",
          "features/name:solution", RECOMMENDED_ACTION);
}
