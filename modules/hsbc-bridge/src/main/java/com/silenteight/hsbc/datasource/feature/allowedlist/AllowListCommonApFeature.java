package com.silenteight.hsbc.datasource.feature.allowedlist;

import com.silenteight.hsbc.datasource.datamodel.CaseInformation;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.allowedlist.AllowListFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class AllowListCommonApFeature
    implements FeatureValuesRetriever<AllowListFeatureInputDto> {

  @Override
  public AllowListFeatureInputDto retrieve(MatchData matchData) {
    var characteristicsValues = getCharacteristicsValues(matchData.getCaseInformation());

    return AllowListFeatureInputDto.builder()
        .feature(getFeatureName())
        .allowListName("hsbc_common_alerted_party")
        .characteristicsValues(characteristicsValues)
        .build();
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
