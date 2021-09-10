package com.silenteight.hsbc.datasource.feature.allowedlist;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.*;
import com.silenteight.hsbc.datasource.dto.allowedlist.AllowListFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
public class AllowListCommonWpFeature implements FeatureValuesRetriever<AllowListFeatureInputDto> {

  @Override
  public AllowListFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var characteristicsValues = getCharacteristicsValues(matchData);

    var result = AllowListFeatureInputDto.builder()
        .feature(getFeatureName())
        .allowListNames(List.of("hsbc_common_watchlist_party"))
        .characteristicsValues(characteristicsValues)
        .build();

    log.debug(
        "Datasource response for feature: {} with allowed list names size {} and characteristics values size {}.",
        result.getFeature(),
        result.getAllowListNames().size(),
        result.getCharacteristicsValues().size());

    return result;
  }

  private List<String> getCharacteristicsValues(MatchData matchData) {
    var values = new ArrayList<String>();
    if (matchData.isIndividual()) {
      values.addAll(extractFromIndividual(matchData));
    } else {
      values.addAll(extractFromEntity(matchData));
    }
    return values;
  }

  private Collection<String> extractFromEntity(EntityComposite entityComposite) {
    var values = new HashSet<String>();
    values.addAll(collectCountryCodes(entityComposite.getCtrpScreeningEntities()));
    values.addAll(collectIds(entityComposite.getPrivateListEntities()));
    values.addAll(collectIds(entityComposite.getWorldCheckEntities()));

    return values;
  }

  private Collection<String> extractFromIndividual(IndividualComposite individualComposite) {
    var values = new HashSet<String>();
    values.addAll(collectCountryCodes(individualComposite.getCtrpScreeningIndividuals()));
    values.addAll(collectIds(individualComposite.getPrivateListIndividuals()));
    values.addAll(collectIds(individualComposite.getWorldCheckIndividuals()));

    return values;
  }

  private Set<String> collectCountryCodes(List<CtrpScreening> ctrpScreenings) {
    if (isEmpty(ctrpScreenings)) {
      return emptySet();
    }

    return ctrpScreenings.stream()
        .map(CtrpScreening::getCountryCode)
        .filter(StringUtils::isNotEmpty)
        .collect(Collectors.toSet());
  }

  private Set<String> collectIds(List<? extends ListRecordId> listRecordIds) {
    if (isEmpty(listRecordIds)) {
      return emptySet();
    }

    return listRecordIds.stream()
        .map(ListRecordId::getListRecordId)
        .filter(StringUtils::isNotEmpty)
        .collect(Collectors.toSet());
  }

  @Override
  public Feature getFeature() {
    return Feature.ALLOW_LIST_COMMON_WP;
  }
}
