package com.silenteight.serp.governance.strategy;

import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
class FixedStrategyProvider implements CurrentStrategyProvider {

  private final SolvingStrategyType currentStrategy;

  @Override
  public Optional<String> getCurrentStrategy() {
    return Optional.of("strategies/" + currentStrategy);
  }
}
