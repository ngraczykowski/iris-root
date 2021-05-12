package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableConfigurationProperties(SolveProperties.class)
@RequiredArgsConstructor
class CleanTimedOutAgentExchangesConfiguration implements SchedulingConfigurer {

  private final SolveProperties solveProperties;
  private final CleanTimedOutAgentExchangesUseCase cleanTimedOutAgentExchangesUseCase;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.addFixedDelayTask(
        cleanTimedOutAgentExchangesUseCase,
        solveProperties.getCleanTimedOutAgentExchangesInterval().toMillis());
  }
}
