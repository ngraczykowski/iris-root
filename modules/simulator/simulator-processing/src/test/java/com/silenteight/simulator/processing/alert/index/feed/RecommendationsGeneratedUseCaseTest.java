package com.silenteight.simulator.processing.alert.index.feed;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.processing.alert.index.feed.FeedFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationsGeneratedUseCaseTest {

  @InjectMocks
  private RecommendationsGeneratedUseCase underTest;

  @Mock
  private RecommendationService recommendationService;
  @Mock
  private IndexedAlertService indexedAlertService;
  @Mock
  private RequestIdGenerator requestIdGenerator;

  @Test
  void shouldHandleMessage() {
    // given
    when(requestIdGenerator.generate()).thenReturn(REQUEST_ID);
    when(recommendationService.getRecommendation(RECOMMENDATION_NAME)).thenReturn(RECOMMENDATION);

    // when
    SimulationDataIndexRequest indexRequest = underTest.handle(REQUEST);

    // then
    verify(indexedAlertService).saveAsSent(
        REQUEST_ID, ANALYSIS_NAME, REQUEST.getRecommendationInfosCount());
    assertThat(indexRequest.getRequestId()).isEqualTo(REQUEST_ID);
    assertThat(indexRequest.getAnalysisName()).isEqualTo(ANALYSIS_NAME);
    assertThat(indexRequest.getAlertsList())
        .extracting(Alert::getDiscriminator).containsExactly(ALERT_NAME);
  }
}
