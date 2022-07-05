/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.simulator.processing.simulation;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Sql
@SpringBootTest(classes = {SimulationRunningConfiguration.class})
@ContextConfiguration(initializers = {PostgresTestInitializer.class})
@ActiveProfiles("jdbc-test")
@AutoConfigureJdbc
class SimulationProcessingIT {

  @Autowired private SimulationFinishScheduler simulationFinishScheduler;
  @MockBean private AnalysisService analysisService;
  @MockBean private SimulationService simulationService;

  @Test
  void testFinishedSimulation() {
    assertThat(simulationFinishScheduler).isNotNull();

    when(analysisService.getAnalysis("analysis/1")).thenReturn(AckMessageFixtures.ANALYSIS_1);
    when(analysisService.getAnalysis("analysis/2"))
        .thenReturn(AckMessageFixtures.ANALYSIS_FINISHED);
    when(analysisService.getAnalysis("analysis/3")).thenReturn(AckMessageFixtures.ANALYSIS_1);

    simulationFinishScheduler.scheduleSimulation();

    verify(simulationService, times(0)).finish("analysis/1");
    verify(simulationService, times(1)).finish("analysis/2");
    verify(simulationService, times(0)).finish("analysis/3");
  }
}
