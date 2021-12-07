package com.silenteight.universaldatasource.api.library.location.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

@Value
@Builder
public class LocationFeatureInputOut implements Feature {

  String feature;
  String alertedPartyLocation;
  String watchlistLocation;

  static LocationFeatureInputOut createFrom(LocationFeatureInput input) {
    return LocationFeatureInputOut.builder()
        .feature(input.getFeature())
        .alertedPartyLocation(input.getAlertedPartyLocation())
        .watchlistLocation(input.getWatchlistLocation())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
