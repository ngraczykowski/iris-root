package com.silenteight.payments.bridge.common.protobuf;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;

import com.google.protobuf.Any;
import com.google.protobuf.Message;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.FEATURE_PREFIX;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentDataSourceUtils {

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
