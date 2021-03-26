package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto;

import java.util.List;

class NationalityIdFeature implements FeatureValuesRetriever<NationalIdFeatureInputDto> {

  @Override
  public NationalIdFeatureInputDto retrieve(MatchRawData matchRawData) {
    return NationalIdFeatureInputDto.builder()
        .feature(getFeatureName())
        .alertedPartyCountry("AP")
        .watchlistCountry("PL")
        .alertedPartyDocumentNumbers(List.of("123"))
        .watchlistDocumentNumbers(List.of("321"))
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.NATIONALITY_ID;
  }
}
