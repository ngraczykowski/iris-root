package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.domain.model.DataRetentionMode;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataRetentionStrategyFactory {

  private final List<DataRetentionStrategy> strategies;

  public DataRetentionStrategy getStrategy(DataRetentionMode mode) {
    return strategies.stream()
        .filter(strategy -> strategy.getSupportedDataRetentionMode() == (mode))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Strategy not found for mode [" + mode + "]"));
  }
}
