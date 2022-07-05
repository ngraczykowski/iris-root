/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.simulator.processing.simulation;

import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
class SimulationRunningConfiguration {

  @Bean
  SimulationFinishChecker simulationFinishChecker(
      SimulationService simulationService, AnalysisService analysisService) {
    return new SimulationFinishChecker(simulationService, analysisService);
  }

  @Bean
  SimulationFinishScheduler simulationScheduler(
      SimulationRunningQuery simulationRunningQuery,
      SimulationFinishChecker simulationFinishChecker) {
    return new SimulationFinishScheduler(simulationRunningQuery, simulationFinishChecker);
  }

  @Bean
  SimulationRunningQuery simulationRunningQuery(JdbcTemplate jdbcTemplate) {
    return new SimulationRunningQueryJdbc(jdbcTemplate);
  }
}
