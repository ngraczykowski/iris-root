package com.silenteight.universaldatasource.api.library.agentinput.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class AgentInputIn<T extends Feature> {

  String name;
  String alert;
  String match;

  @Builder.Default
  List<T> featureInputs = List.of();

  AgentInput createFrom() {
    return AgentInput.newBuilder()
        .setName(name)
        .setAlert(alert)
        .setMatch(match)
        .addAllFeatureInputs(featureInputs
            .stream()
            .map(this::toFeatureInput)
            .collect(Collectors.toList())
        )
        .build();
  }

  FeatureInput toFeatureInput(T feature) {
    FeatureInput.Builder builder = FeatureInput.newBuilder();
    feature.accept(FeatureBuilder.INSTANCE, builder);
    return builder.build();
  }
}
