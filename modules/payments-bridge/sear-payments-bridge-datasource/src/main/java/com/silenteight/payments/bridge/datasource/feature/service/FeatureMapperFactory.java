package com.silenteight.payments.bridge.datasource.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.feature.port.outgoing.FeatureMapper;

import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
class FeatureMapperFactory {

  private final Map<String, FeatureMapper> mappers;

  FeatureMapper get(String agentType) {
    if (!mappers.containsKey(agentType))
      throw new NoSuchElementException();

    return mappers.get(agentType);
  }
}
