package com.silenteight.hsbc.datasource.feature.allowedlist;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.CaseInformation;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.allowedlist.AllowListFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
public class AllowListCommonApFeature implements FeatureValuesRetriever<AllowListFeatureInputDto> {

  @Override
  public AllowListFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var characteristicsValues = getCharacteristicsValues(matchData.getCaseInformation());

    var result = AllowListFeatureInputDto.builder()
        .feature(getFeatureName())
        .allowListNames(List.of("hsbc_common_alerted_party"))
        .characteristicsValues(characteristicsValues)
        .build();

    log.debug(
        "Datasource response for feature: {} with allowed list names size {} and characteristics values size {}.",
        result.getFeature(),
        result.getAllowListNames().size(),
        result.getCharacteristicsValues().size());

    return result;
  }

  private List<String> getCharacteristicsValues(CaseInformation caseInformation) {
    var parentId = caseInformation.getParentId();

    return isNotEmpty(parentId) ? List.of(parentId) : emptyList();
  }

  @Override
  public Feature getFeature() {
    return Feature.ALLOW_LIST_COMMON_AP;
  }
}
