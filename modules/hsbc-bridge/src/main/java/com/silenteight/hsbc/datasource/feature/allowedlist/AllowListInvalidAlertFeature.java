package com.silenteight.hsbc.datasource.feature.allowedlist;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.allowedlist.AllowListFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;

import java.util.List;

@Slf4j
public class AllowListInvalidAlertFeature extends AllowListCommonNameFeature {

  @Override
  public AllowListFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var characteristicsValues = getCharacteristicsValues(matchData);

    var result = AllowListFeatureInputDto.builder()
        .feature(getFeatureName())
        .allowListNames(List.of("hsbc_invalid_names", "hsbc_financial_institution"))
        .characteristicsValues(characteristicsValues)
        .build();

    log.debug(
        "Datasource response for feature: {} with allowed list names size {} and characteristics values size {}.",
        result.getFeature(),
        result.getAllowListNames().size(),
        result.getCharacteristicsValues().size());

    return result;
  }

  @Override
  public Feature getFeature() {
    return Feature.ALLOW_LIST_INVALID_ALERT;
  }
}
