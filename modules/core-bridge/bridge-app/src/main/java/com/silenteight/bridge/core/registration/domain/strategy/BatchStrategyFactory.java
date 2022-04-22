package com.silenteight.bridge.core.registration.domain.strategy;

import com.silenteight.bridge.core.registration.domain.command.RegisterBatchCommand;
import com.silenteight.bridge.core.registration.domain.model.Batch;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BatchStrategyFactory {

  private final Map<BatchStrategyName, BatchRegistrationStrategy> registrationBatchStrategies;
  private final Map<BatchStrategyName, UdsFedAlertsProcessorStrategy>
      udsFedAlertsProcessorStrategies;

  public BatchStrategyFactory(
      Set<BatchRegistrationStrategy> batchRegistrationStrategySet,
      Set<UdsFedAlertsProcessorStrategy> alertAnalysisStrategySet) {
    registrationBatchStrategies = toMap(batchRegistrationStrategySet);
    udsFedAlertsProcessorStrategies = toMap(alertAnalysisStrategySet);
  }

  public BatchRegistrationStrategy getStrategyForRegistration(RegisterBatchCommand command) {
    return getSolvingOrSimulationStrategy(registrationBatchStrategies, command.isSimulation());
  }

  public UdsFedAlertsProcessorStrategy getStrategyForUdsFedAlertsProcessor(Batch batch) {
    return getSolvingOrSimulationStrategy(udsFedAlertsProcessorStrategies, batch.isSimulation());
  }

  private <T extends BatchStrategyNameProvider> Map<BatchStrategyName, T> toMap(Set<T> strategies) {
    return strategies
        .stream()
        .collect(Collectors.toMap(T::getStrategyName, Function.identity()));
  }

  private <T extends BatchStrategyNameProvider> T getSolvingOrSimulationStrategy(
      Map<BatchStrategyName, T> strategies, boolean isSimulation) {
    if (isSimulation) {
      return getStrategy(strategies, BatchStrategyName.SIMULATION);
    }
    return getStrategy(strategies, BatchStrategyName.SOLVING);
  }

  private <T> T getStrategy(Map<BatchStrategyName, T> strategies, BatchStrategyName strategyName) {
    return Optional.ofNullable(strategies.get(strategyName))
        .orElseThrow(
            () -> new IllegalStateException("No batch strategy found for " + strategyName.name()));
  }
}
