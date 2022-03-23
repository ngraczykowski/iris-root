package com.silenteight.payments.bridge.common.protobuf;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;

import com.google.protobuf.Any;
import com.google.protobuf.Message;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentDataSourceUtils {

  public static <T extends Message> FeatureInput createFeatureInput(
      String feature, T featureInput) {
    return FeatureInput
        .newBuilder()
        .setFeature(feature)
        .setAgentFeatureInput(Any.pack(featureInput))
        .build();
  }

}
