package com.silenteight.payments.bridge.common.protobuf;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;

import com.google.protobuf.Any;
import com.google.protobuf.Message;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentDataSourceUtils {

  private static final String FEATURE_PREFIX = "features/";

  public static <T extends Message> FeatureInput createFeatureInput(
      String feature, T featureInput) {
    return FeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName(feature))
        .setAgentFeatureInput(Any.pack(featureInput))
        .build();
  }

  public static String getFullFeatureName(String feature) {
    return FEATURE_PREFIX + feature;
  }

}
