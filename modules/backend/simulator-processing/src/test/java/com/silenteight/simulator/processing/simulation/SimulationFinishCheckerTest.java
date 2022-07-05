/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.simulator.processing.simulation;

import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.domain.State;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;

import static com.silenteight.simulator.processing.alert.index.domain.State.ACKED;
import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;
import static com.silenteight.simulator.processing.simulation.AckMessageFixtures.ANALYSIS_1;
import static com.silenteight.simulator.processing.simulation.AckMessageFixtures.ANALYSIS_NAME;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationFinishCheckerTest {

  @Mock private SimulationService simulationService;
  @Mock private AnalysisService analysisService;
  private SimulationFinishChecker simulationFinishChecker;

  @DisplayName("When some alert indexed, SimulationService shouldn't call finish method")
  @Test
  void ifSimulationHasLessAlertIndexShouldNotExecuteFinish() {
    // given
    var spy = spy(simulationService);
    when(analysisService.getAnalysis(ANALYSIS_NAME)).thenReturn(ANALYSIS_1);

    simulationFinishChecker = new SimulationFinishChecker(spy, analysisService);
    EnumMap<State, Long> statuses = new EnumMap<>(State.class);
    statuses.put(ACKED, 10L);
    statuses.put(SENT, 10L);

    // when
    simulationFinishChecker.check(ANALYSIS_NAME, statuses);

    // then
    verify(spy, times(0)).finish(any());
  }

  @DisplayName("When all alert indexed, SimulationService should call finish method")
  @Test
  void ifSimulationHasAllAlertIndexShouldExecuteFinish() {
    // given
    var spy = spy(simulationService);
    when(analysisService.getAnalysis(ANALYSIS_NAME)).thenReturn(ANALYSIS_1);
    doNothing().when(spy).finish(ANALYSIS_NAME);

    simulationFinishChecker = new SimulationFinishChecker(spy, analysisService);
    EnumMap<State, Long> statuses = new EnumMap<>(State.class);
    statuses.put(ACKED, 20L);

    //  when
    simulationFinishChecker.check(ANALYSIS_NAME, statuses);

    // then
    verify(spy, times(1)).finish(ANALYSIS_NAME);
  }
}
