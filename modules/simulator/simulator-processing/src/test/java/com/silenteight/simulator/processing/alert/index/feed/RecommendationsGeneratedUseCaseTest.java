package com.silenteight.simulator.processing.alert.index.feed;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.amqp.gateway.SimulationDataIndexRequestGateway;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Stack;

import static com.silenteight.adjudication.api.v1.Analysis.State.DONE;
import static com.silenteight.adjudication.api.v1.Analysis.State.RUNNING;
import static com.silenteight.simulator.processing.alert.index.fixtures.RecommendationFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationsGeneratedUseCaseTest {

  private static final int BATCH_SIZE = 5;

  private RecommendationsGeneratedUseCase underTest;
  @Mock
  private SimulationService simulationService;
  @Mock
  private RecommendationService recommendationService;
  @Mock
  private IndexedAlertService indexedAlertService;
  @Mock
  private RequestIdGenerator requestIdGenerator;
  @Mock
  private AnalysisService analysisService;
  @Mock
  private SimulationDataIndexRequestGateway gateway;

  @BeforeEach
  void setUp() {
    underTest = new RecommendationsGeneratedUseCase(
        simulationService, recommendationService, indexedAlertService, requestIdGenerator,
        analysisService, gateway, BATCH_SIZE);
  }

  @Test
  void shouldHandleMessageWhenSimulationExistsAndRunning() {
    // given
    Analysis analysis = mock(Analysis.class);
    when(analysis.getState()).thenReturn(RUNNING);
    Stack<RecommendationWithMetadata> recommendations = new Stack<>();
    recommendations.add(RECOMMENDATION_WITH_METADATA);
    when(simulationService.exists(ANALYSIS_NAME)).thenReturn(true);
    when(analysisService.getAnalysis(ANALYSIS_NAME)).thenReturn(analysis);

    // when
    underTest.handle(REQUEST);
    //then
    verify(analysisService.getAnalysis(ANALYSIS_NAME)).getState();
    verifyNoInteractions(recommendationService);
    verifyNoInteractions(indexedAlertService);
  }

  @Test
  void shouldHandleMessageWhenSimulationDoesNotExist() {
    // given
    when(simulationService.exists(ANALYSIS_NAME)).thenReturn(false);

    // when
    underTest.handle(REQUEST);

    // then
    verifyNoInteractions(recommendationService);
    verifyNoInteractions(indexedAlertService);
  }

  @Test
  void shouldHandleMessageWithMultipleAlertsWhenSimulationDone() {
    // given
    Analysis analysis = mock(Analysis.class);
    when(analysis.getState()).thenReturn(DONE);
    Stack<RecommendationWithMetadata> recommendations = new Stack<>();
    recommendations.addAll(RECOMMENDATION_WITH_METADATA_LIST);
    when(simulationService.exists(ANALYSIS_NAME)).thenReturn(true);
    when(requestIdGenerator.generate()).thenReturn(REQUEST_ID);
    when(analysisService.getAnalysis(ANALYSIS_NAME)).thenReturn(analysis);
    when(recommendationService.streamRecommendationsWithMetadata(ANALYSIS_NAME))
        .thenReturn(new RecommendationWithMetaDataIterator(recommendations));
    // when
    underTest.handle(MULTIPLE_ALERTS_REQUEST);
    // then
    ArgumentCaptor<String> requestId = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> analysisName = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Long> alertCount = ArgumentCaptor.forClass(Long.class);
    verify(recommendationService, times(1))
        .streamRecommendationsWithMetadata(ANALYSIS_NAME);
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
}
