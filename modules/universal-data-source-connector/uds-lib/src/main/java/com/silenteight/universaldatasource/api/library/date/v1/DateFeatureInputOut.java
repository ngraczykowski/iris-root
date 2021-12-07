package com.silenteight.universaldatasource.api.library.date.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.date.v1.DateFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;

@Value
@Builder
public class DateFeatureInputOut implements Feature {

  String feature;

  @Builder.Default
  List<String> alertedPartyDates = List.of();

  @Builder.Default
  List<String> watchlistDates = List.of();

  EntityTypeOut alertedPartyType;
  SeverityModeOut mode;

  static DateFeatureInputOut createFrom(DateFeatureInput input) {
    return DateFeatureInputOut
        .builder()
        .feature(input.getFeature())
        .alertedPartyDates(input.getAlertedPartyDatesList())
        .watchlistDates(input.getWatchlistDatesList())
        .alertedPartyType(EntityTypeOut.createFrom(input.getAlertedPartyType()))
        .mode(SeverityModeOut.createFrom(input.getMode()))
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
