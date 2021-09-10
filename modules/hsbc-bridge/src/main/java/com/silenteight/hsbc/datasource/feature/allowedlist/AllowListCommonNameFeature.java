package com.silenteight.hsbc.datasource.feature.allowedlist;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity;
import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.allowedlist.AllowListFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;

@Slf4j
public class AllowListCommonNameFeature
    implements FeatureValuesRetriever<AllowListFeatureInputDto> {

  @Override
  public AllowListFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var characteristicsValues = getCharacteristicsValues(matchData);

    var result = AllowListFeatureInputDto.builder()
        .feature(getFeatureName())
        .allowListNames(List.of("hsbc_common_name"))
        .characteristicsValues(characteristicsValues)
        .build();

    log.debug(
        "Datasource response for feature: {} with allowed list names size {} and characteristics values size {}.",
        result.getFeature(),
        result.getAllowListNames().size(),
        result.getCharacteristicsValues().size());
    
    return result;
  }

  protected List<String> getCharacteristicsValues(MatchData matchData) {
    var values = new ArrayList<String>();
    if (matchData.isIndividual()) {
      values.addAll(extractNamesFromIndividual(matchData.getCustomerIndividual()));
    } else {
      values.addAll(extractNamesFromEntity(matchData.getCustomerEntity()));
    }
    return values;
  }

  private Collection<String> extractNamesFromEntity(CustomerEntity customerEntity) {
    return toDistinctList(
        Stream.of(
            customerEntity.getEntityName(),
            customerEntity.getEntityNameOriginal(),
            customerEntity.getOriginalScriptName()
        ).filter(StringUtils::isNotEmpty));
  }

  private Collection<String> extractNamesFromIndividual(CustomerIndividual customerIndividual) {
    return toDistinctList(
        Stream.of(
            customerIndividual.getGivenName(),
            customerIndividual.getFamilyNameOriginal(),
            customerIndividual.getFullNameDerived(),
            customerIndividual.getMiddleName(),
            customerIndividual.getOriginalScriptName(),
            customerIndividual.getProfileFullName()
        ).filter(StringUtils::isNotEmpty));
  }

  @Override
  public Feature getFeature() {
    return Feature.ALLOW_LIST_COMMON_NAME;
  }
}
