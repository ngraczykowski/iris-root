package com.silenteight.simulator.management.timeout;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.management.list.ListSimulationsQuery;
import com.silenteight.simulator.management.progress.IndexedAlertProvider;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.validation.Valid;

import static java.util.List.of;

@Configuration
@EnableConfigurationProperties(SimulationTimeoutProperties.class)
@EnableScheduling
class SimulationTimeoutConfiguration {

  @Bean
  SimulationTimeoutService timeoutService(
      ListSimulationsQuery listSimulationsQuery,
      SimulationService simulationService,
      AnalysisTimeoutValidator analysisTimeoutValidator,
      IndexingTimeoutValidator indexingTimeoutValidator,
      UpdateTimeoutValidator updateTimeoutValidator
  ) {
    return new SimulationTimeoutService(
        listSimulationsQuery,
        simulationService,
        of(updateTimeoutValidator, indexingTimeoutValidator, analysisTimeoutValidator));
  }

  @Bean
  SimulationTimeoutJobScheduler simulationTimeoutJobScheduler(
      SimulationTimeoutService timeoutService) {

    return new SimulationTimeoutJobScheduler(timeoutService);
  }

  @Bean
  AnalysisTimeoutValidator analysisTimeoutValidator(
      AnalysisService analysisService,
      SimulationService simulationService) {
    return new AnalysisTimeoutValidator(analysisService, simulationService);
  }

  @Bean
  IndexingTimeoutValidator indexingTimeoutValidator(
      TimeSource timeSource,
      IndexedAlertProvider indexedAlertProvider,
      @Valid SimulationTimeoutProperties timeoutProperties) {

    return new IndexingTimeoutValidator(timeSource, indexedAlertProvider,
        timeoutProperties.getDurationTime());
  }

  @Bean
  UpdateTimeoutValidator updateTimeoutValidator(
      TimeSource timeSource,
      @Valid SimulationTimeoutProperties timeoutProperties) {

    return new UpdateTimeoutValidator(timeSource, timeoutProperties.getDurationTime());
  }
}
