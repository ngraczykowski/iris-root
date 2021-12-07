package com.silenteight.universaldatasource.api.library.freetext.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.freetext.v1.FreeTextFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;

@Value
@Builder
public class FreeTextFeatureInputOut implements Feature {

  String feature;
  String matchedName;
  String matchedNameSynonym;
  String matchedType;

  @Builder.Default
  List<String> matchingTexts = List.of();
  String freeText;

  static FreeTextFeatureInputOut createFrom(FreeTextFeatureInput input) {
    return FreeTextFeatureInputOut.builder()
        .feature(input.getFeature())
        .matchedName(input.getMatchedName())
        .matchedNameSynonym(input.getMatchedNameSynonym())
        .matchedType(input.getMatchedType())
        .matchingTexts(input.getMatchingTextsList())
        .feature(input.getFreetext())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
