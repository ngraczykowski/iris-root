package com.silenteight.simulator.management.progress;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.simulator.management.create.AnalysisService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.silenteight.adjudication.api.v1.Analysis.State.RUNNING;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationProgressServiceTest {

  @InjectMocks
  private SimulationProgressService underTest;
  @Mock
  private AnalysisService analysisService;
  @Mock
  private IndexedAlertProvider indexedAlertProvider;
  @Mock
  private AnalysisNameQuery analysisNameQuery;

  private static final UUID SIMULATION_ID = UUID.randomUUID();
  private static final String ANALYSIS_NAME = "analysis/4ebd9736-5072-4915-8a48-95ab3054c93f";
  private static final long INDEXED_ALERTS = 100L;
  private static final Analysis ANALYSIS = Analysis.newBuilder()
      .setState(RUNNING)
      .setAlertCount(124)
      .setPendingAlerts(37)
      .build();

  public static final SimulationProgressDto ANALYSIS_STATISTIC_DTO_1 =
      SimulationProgressDto.builder()
          .allAlerts(124)
          .solvedAlerts(87)
          .indexedAlerts(100)
          .build();

  @Test
  void shouldReturnAnalysisStatus() {
    //when
    when(analysisNameQuery.getAnalysisName(SIMULATION_ID)).thenReturn(ANALYSIS_NAME);
    when(analysisService.getAnalysis(ANALYSIS_NAME)).thenReturn(ANALYSIS);
    when(indexedAlertProvider.getAllIndexedAlertsCount(ANALYSIS_NAME))
        .thenReturn(INDEXED_ALERTS);

    //then
    SimulationProgressDto simulationProgressDto = underTest.getProgress(SIMULATION_ID);
    assertThat(simulationProgressDto).isEqualTo(ANALYSIS_STATISTIC_DTO_1);
  }
}
