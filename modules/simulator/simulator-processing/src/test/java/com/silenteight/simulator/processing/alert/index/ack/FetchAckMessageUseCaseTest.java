package com.silenteight.simulator.processing.alert.index.ack;

import com.silenteight.simulator.dataset.domain.DatasetMetadataService;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.details.SimulationDetailsQuery;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertQuery;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.processing.alert.index.ack.AckMessageFixtures.*;
import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;
import static java.util.List.of;
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
  private SimulationDetailsQuery simulationQuery;
  @Mock
  private DatasetMetadataService datasetService;
  @Mock
  private SimulationService simulationService;
  @Mock
  private IndexedAlertQuery indexedAlertQuery;

  @Test
  void shouldFinishSimulation() {
    // given
    doNothing().when(indexedAlertService).ack(any());
    when(indexedAlertQuery.getAnalysisNameByRequestId(any())).thenReturn(ANALYSIS_NAME);
    when(analysisService.getAnalysis(ANALYSIS_NAME)).thenReturn(ANALYSIS);
    when(indexedAlertQuery.count(ANALYSIS_NAME, of(SENT)))
        .thenReturn(0L);

    when(indexedAlertQuery.sumAllAlertsCountWithAnalysisName(ANALYSIS_NAME)).thenReturn(10L);
    when(simulationQuery.get(ANALYSIS_NAME)).thenReturn(SIMULATION_DETAILS);
    when(datasetService.countAllAlerts(DATASETS)).thenReturn(10L);

    // when
    underTest.handle(INDEX_RESPONSE);

    // then
    verify(simulationService, times(1)).finish(ANALYSIS_NAME);
  }
}
