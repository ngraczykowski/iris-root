package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.api.v1.Analysis;

@Value
@Builder
public class Feature {

  String feature;

  String agentConfig;

  public Analysis.Feature toAnalysisFeature() {
    return Analysis.Feature
        .newBuilder()
        .setFeature(feature)
        .setAgentConfig(agentConfig)
        .build();
  }
}
