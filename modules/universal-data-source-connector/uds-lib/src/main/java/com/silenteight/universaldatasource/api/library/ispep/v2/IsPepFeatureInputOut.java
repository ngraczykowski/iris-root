package com.silenteight.universaldatasource.api.library.ispep.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.ispep.v2.IsPepFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

@Value
@Builder
public class IsPepFeatureInputOut implements Feature {

  String feature;
  WatchlistItemOut watchlistItem;
  AlertedPartyItemOut alertedPartyItem;

  static IsPepFeatureInputOut createFrom(IsPepFeatureInput input) {
    return IsPepFeatureInputOut.builder()
        .feature(input.getFeature())
        .watchlistItem(WatchlistItemOut.createFrom(input.getWatchlistItem()))
        .alertedPartyItem(AlertedPartyItemOut.createFrom(input.getAlertedPartyItem()))
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
