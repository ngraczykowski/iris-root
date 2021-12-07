package com.silenteight.universaldatasource.api.library.allowlist.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.allowlist.v1.AllowListFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;

@Value
@Builder
public class AllowListFeatureInputOut implements Feature {

  String feature;

  @Builder.Default
  List<String> characteristicsValues = List.of();

  @Builder.Default
  List<String> allowListName = List.of();

  static AllowListFeatureInputOut createFrom(AllowListFeatureInput input) {
    return AllowListFeatureInputOut
        .builder()
        .feature(input.getFeature())
        .characteristicsValues(input.getCharacteristicsValuesList())
        .allowListName(input.getAllowListNameList())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
