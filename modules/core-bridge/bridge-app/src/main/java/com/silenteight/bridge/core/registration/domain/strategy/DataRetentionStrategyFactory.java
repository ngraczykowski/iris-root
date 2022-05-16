package com.silenteight.bridge.core.registration.domain.strategy;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.domain.model.DataRetentionType;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataRetentionStrategyFactory {

  private final List<DataRetentionStrategy> strategies;

  public DataRetentionStrategy getStrategy(DataRetentionType type) {
    return strategies.stream()
        .filter(strategy -> strategy.getSupportedDataRetentionTypes().contains(type))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Strategy not found for type [" + type + "]"));
  }
}
