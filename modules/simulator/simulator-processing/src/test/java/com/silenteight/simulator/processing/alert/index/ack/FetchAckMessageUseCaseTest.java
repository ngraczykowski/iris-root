package com.silenteight.simulator.processing.alert.index.ack;

import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertQuery;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.processing.alert.index.ack.AckMessageFixtures.ANALYSIS;
import static com.silenteight.simulator.processing.alert.index.ack.AckMessageFixtures.ANALYSIS_NAME;
import static com.silenteight.simulator.processing.alert.index.ack.AckMessageFixtures.INDEX_RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchAckMessageUseCaseTest {

  @InjectMocks
  private FetchAckMessageUseCase underTest;
  @Mock
  private AnalysisService analysisService;
  @Mock
  private IndexedAlertService indexedAlertService;
  @Mock
  private IndexedAlertQuery indexedAlertQuery;
  @Mock
  private SimulationService simulationService;

  @Test
  void shouldCallMethodMarkAsDone() {
    // given
    doNothing().when(indexedAlertService).ack(any());
    when(indexedAlertQuery.getAnalysisNameByRequestId(any())).thenReturn(ANALYSIS_NAME);
    when(analysisService.getAnalysis(ANALYSIS_NAME)).thenReturn(ANALYSIS);
    when(indexedAlertQuery.areAllIndexedAlertsAcked(ANALYSIS_NAME)).thenReturn(true);
    when(indexedAlertQuery.sumAllAlertsCountWithAnalysisName(ANALYSIS_NAME)).thenReturn(10L);
    when(simulationService.countAllAlerts(ANALYSIS_NAME)).thenReturn(10L);

    // when
    underTest.handle(INDEX_RESPONSE);

    // then
    verify(simulationService, times(1)).finish(ANALYSIS_NAME);
  }
}
