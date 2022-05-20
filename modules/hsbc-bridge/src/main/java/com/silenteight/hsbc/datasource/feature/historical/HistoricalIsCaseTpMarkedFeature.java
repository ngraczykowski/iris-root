package com.silenteight.hsbc.datasource.feature.historical;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalDecisionsFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

@Slf4j
@RequiredArgsConstructor
public class HistoricalIsCaseTpMarkedFeature
    implements FeatureValuesRetriever<HistoricalDecisionsFeatureInputDto> {

  private final HistoricalDecisionsQuery.Factory queryFactory;

  @Override
  public HistoricalDecisionsFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = queryFactory.create(matchData);
    var inputBuilder = HistoricalDecisionsFeatureInputDto.builder();

    var modelKeyDto = query.getCaseTpMarkedInput();

    modelKeyDto.ifPresent(modelKey -> inputBuilder
        .modelKey(modelKey)
        .discriminator(HistoricalDecisionsQuery.DISCRIMINATOR)
    );

    var result = inputBuilder.feature(getFeatureName()).build();

    log.debug(
        "Datasource response for feature: {} with modelKeyType {} and discriminator {}.",
        result.getFeature(),
        result.getModelKey().getModelKeyType(),
        result.getDiscriminator());

    return result;
  }

  @Override
  public Feature getFeature() {
    return Feature.HISTORICAL_IS_CASE_TP_MARKED;
  }
}
