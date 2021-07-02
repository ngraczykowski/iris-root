package com.silenteight.hsbc.datasource.feature.allowedlist;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.allowedlist.AllowListFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;

public class AllowListFeature implements FeatureValuesRetriever<AllowListFeatureInputDto> {

  @Override
  public AllowListFeatureInputDto retrieve(MatchData matchData) {
    return AllowListFeatureInputDto.builder()
        .feature(getFeatureName())
        .allowListName("allowListName")
        .characteristicsValues(List.of("char1", "char2"))
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.ALLOW_LIST;
  }
}
