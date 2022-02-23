package com.silenteight.universaldatasource.app.feature.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class BatchFeatureRequest {

  String agentInputType;

  List<String> matches;

  List<String> features;

  public BatchFeatureRequest mapRequestFeatures(List<String> features) {
    return BatchFeatureRequest.builder()
        .agentInputType(agentInputType)
        .matches(matches)
        .features(features)
        .build();
  }
}
