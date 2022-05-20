package com.silenteight.universaldatasource.app.feature.service;

import lombok.RequiredArgsConstructor;


import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureMapper;

import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
class FeatureMapperFactory {

  private final Map<String, FeatureMapper> mappers;

  FeatureMapper get(String agentType) {
    if (!mappers.containsKey(agentType))
      throw new NoSuchElementException(
          "No feature mapper was found for agent input type of: " + agentType);

    return mappers.get(agentType);
  }
}
