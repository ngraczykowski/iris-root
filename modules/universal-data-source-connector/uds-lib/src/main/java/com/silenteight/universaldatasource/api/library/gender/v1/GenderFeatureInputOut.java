package com.silenteight.universaldatasource.api.library.gender.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.gender.v1.GenderFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class GenderFeatureInputOut implements Feature {

  String feature;

  @Builder.Default
  List<String> alertedPartyGenders = List.of();

  @Builder.Default
  List<String> watchlistGenders = List.of();

  static GenderFeatureInputOut createFrom(GenderFeatureInput input) {
    return GenderFeatureInputOut.builder()
        .feature(input.getFeature())
        .alertedPartyGenders(input.getAlertedPartyGendersList())
        .watchlistGenders(input.getWatchlistGendersList())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
