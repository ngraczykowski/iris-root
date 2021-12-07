package com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;

@Value
@Builder
public class BankIdentificationCodesFeatureInputOut implements Feature {

  String feature;
  String alertedPartyMatchingField;
  String watchListMatchingText;
  String watchlistType;

  @Builder.Default
  List<String> watchlistSearchCodes = List.of();

  @Builder.Default
  List<String> watchlistBicCodes = List.of();

  static BankIdentificationCodesFeatureInputOut createFrom(
      BankIdentificationCodesFeatureInput request) {
    return BankIdentificationCodesFeatureInputOut.builder()
        .feature(request.getFeature())
        .alertedPartyMatchingField(request.getAlertedPartyMatchingField())
        .watchListMatchingText(request.getWatchlistMatchingText())
        .watchlistSearchCodes(request.getWatchlistSearchCodesList())
        .watchlistBicCodes(request.getWatchlistBicCodesList())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
