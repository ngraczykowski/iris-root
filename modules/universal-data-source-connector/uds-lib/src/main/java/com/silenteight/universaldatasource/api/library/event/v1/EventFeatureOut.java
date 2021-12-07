package com.silenteight.universaldatasource.api.library.event.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.event.v1.EventFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;

@Value
@Builder
public class EventFeatureOut implements Feature {

  String feature;

  @Builder.Default
  List<String> alertedPartyDates = List.of();

  @Builder.Default
  List<String> watchlistEvents = List.of();

  static EventFeatureOut createFrom(EventFeatureInput input) {
    return EventFeatureOut.builder()
        .feature(input.getFeature())
        .alertedPartyDates(input.getAlertedPartyDatesList())
        .watchlistEvents(input.getWatchlistEventsList())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
