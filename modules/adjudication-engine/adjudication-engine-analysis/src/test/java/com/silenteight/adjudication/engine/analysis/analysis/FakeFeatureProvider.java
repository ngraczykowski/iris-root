package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureDto;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FakeFeatureProvider implements FeatureProvider {

  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public List<AgentConfigFeatureDto> getFeatures(List<Feature> features) {
    return features
        .stream()
        .map(f -> new AgentConfigFeatureDto(
            idGenerator.getAndIncrement(), f.getAgentConfig(), f.getFeature()))
        .collect(Collectors.toUnmodifiableList());
  }
}
