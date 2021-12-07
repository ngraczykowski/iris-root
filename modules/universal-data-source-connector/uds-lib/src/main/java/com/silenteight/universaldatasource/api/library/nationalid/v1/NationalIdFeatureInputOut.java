package com.silenteight.universaldatasource.api.library.nationalid.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.nationalid.v1.NationalIdFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;

@Value
@Builder
public class NationalIdFeatureInputOut implements Feature {

  String feature;

  @Builder.Default
  List<String> alertedPartyDocumentNumbers = List.of();

  @Builder.Default
  List<String> watchlistDocumentNumbers = List.of();

  @Builder.Default
  List<String> alertedPartyCountries = List.of();

  @Builder.Default
  List<String> watchlistCountries = List.of();

  static NationalIdFeatureInputOut createFrom(NationalIdFeatureInput input) {
    return NationalIdFeatureInputOut.builder()
        .feature(input.getFeature())
        .alertedPartyDocumentNumbers(input.getAlertedPartyCountriesList())
        .watchlistDocumentNumbers(input.getWatchlistDocumentNumbersList())
        .alertedPartyCountries(input.getAlertedPartyCountriesList())
        .watchlistCountries(input.getWatchlistCountriesList())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
