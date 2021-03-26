package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.dto.gender.GenderFeatureInputDto;

import java.util.List;

class GenderFeature implements FeatureValuesRetriever<GenderFeatureInputDto> {

  @Override
  public GenderFeatureInputDto retrieve(MatchRawData matchRawData) {
    return GenderFeatureInputDto.builder()
        .feature(getFeatureName())
        .alertedPartyGenders(List.of("M", "F"))
        .watchlistGenders(List.of("F"))
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.GENDER;
  }
}
