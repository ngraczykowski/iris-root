package com.silenteight.hsbc.datasource.feature.allowedlist;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.allowedlist.AllowListFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;

import java.util.List;

public class AllowListInvalidAlertFeature extends AllowListCommonNameFeature {

  @Override
  public AllowListFeatureInputDto retrieve(MatchData matchData) {
    var characteristicsValues = getCharacteristicsValues(matchData);

    return AllowListFeatureInputDto.builder()
        .feature(getFeatureName())
        .allowListNames(List.of("hsbc_invalid_names", "hsbc_financial_institution"))
        .characteristicsValues(characteristicsValues)
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.ALLOW_LIST_INVALID_ALERT;
  }
}
