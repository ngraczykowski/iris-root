package com.silenteight.universaldatasource.api.library.name.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class NameFeatureInputOut implements Feature {

  String feature;

  @Builder.Default
  List<AlertedPartyNameOut> alertedPartyNames = List.of();

  @Builder.Default
  List<WatchlistNameOut> watchlistNames = List.of();

  EntityTypeOut alertedPartyType;

  @Builder.Default
  List<String> matchingTexts = List.of();

  static NameFeatureInputOut createFrom(NameFeatureInput input) {
    return NameFeatureInputOut.builder()
        .feature(input.getFeature())
        .alertedPartyNames(input.getAlertedPartyNamesList().stream()
            .map(AlertedPartyNameOut::createFrom)
            .collect(Collectors.toList()))
        .watchlistNames(input.getWatchlistNamesList().stream()
            .map(WatchlistNameOut::createFrom)
            .collect(Collectors.toList()))
        .alertedPartyType(EntityTypeOut.valueOf(input.getAlertedPartyType().name()))
        .matchingTexts(input.getMatchingTextsList())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
