package com.silenteight.universaldatasource.api.library.country.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.country.v1.CountryFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;

@Value
@Builder
public class CountryFeatureInputOut implements Feature {

  String feature;

  @Builder.Default
  List<String> alertedPartyCountries = List.of();

  @Builder.Default
  List<String> watchlistCountries = List.of();

  static CountryFeatureInputOut createFrom(CountryFeatureInput input) {
    return CountryFeatureInputOut.builder()
        .feature(input.getFeature())
        .alertedPartyCountries(input.getAlertedPartyCountriesList())
        .watchlistCountries(input.getWatchlistCountriesList())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
