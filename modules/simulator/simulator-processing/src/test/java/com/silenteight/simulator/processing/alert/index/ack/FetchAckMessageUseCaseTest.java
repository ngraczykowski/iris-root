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

import static com.silenteight.simulator.processing.alert.index.ack.AckMessageFixtures.ANALYSIS_1;
import static com.silenteight.simulator.processing.alert.index.ack.AckMessageFixtures.ANALYSIS_2;
import static com.silenteight.simulator.processing.alert.index.ack.AckMessageFixtures.ANALYSIS_NAME;
import static com.silenteight.simulator.processing.alert.index.ack.AckMessageFixtures.INDEX_RESPONSE;
import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;
import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchAckMessageUseCaseTest {

  @InjectMocks
  private FetchAckMessageUseCase underTest;

  @Mock
  private IndexedAlertService indexedAlertService;
  @Mock
  private SimulationService simulationService;
  @Mock
  private IndexedAlertQuery indexedAlertQuery;
  @Mock
  private AnalysisService analysisService;

  @Test
  void shouldNotFinishSimulation() {
    // given
    doNothing().when(indexedAlertService).ack(any());
    when(indexedAlertQuery.getAnalysisNameByRequestId(any())).thenReturn(ANALYSIS_NAME);
    when(indexedAlertQuery.count(ANALYSIS_NAME, of(SENT))).thenReturn(0L);
    when(indexedAlertQuery.getAllIndexedAlertsCount(ANALYSIS_NAME)).thenReturn(10L);
    when(analysisService.getAnalysis(ANALYSIS_NAME)).thenReturn(ANALYSIS_1);

    // when
    underTest.handle(INDEX_RESPONSE);

    // then
    verify(simulationService, times(0)).finish(ANALYSIS_NAME);
  }

  @Test
  void shouldFinishSimulation() {
    // given
    doNothing().when(indexedAlertService).ack(any());
    when(indexedAlertQuery.getAnalysisNameByRequestId(any())).thenReturn(ANALYSIS_NAME);
    when(indexedAlertQuery.count(ANALYSIS_NAME, of(SENT))).thenReturn(0L);
    when(indexedAlertQuery.getAllIndexedAlertsCount(ANALYSIS_NAME)).thenReturn(42L);
    when(analysisService.getAnalysis(ANALYSIS_NAME)).thenReturn(ANALYSIS_2);

    // when
    underTest.handle(INDEX_RESPONSE);

    // then
    verify(simulationService, times(1)).finish(ANALYSIS_NAME);
  }
}
