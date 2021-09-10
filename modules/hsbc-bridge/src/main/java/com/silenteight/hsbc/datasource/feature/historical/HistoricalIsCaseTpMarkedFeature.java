package com.silenteight.hsbc.datasource.feature.historical;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalFeatureInputDto;
import com.silenteight.hsbc.datasource.extractors.historical.HistoricalDecisionsServiceClient;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.HistoricalFeatureClientValuesRetriever;

@Slf4j
@RequiredArgsConstructor
public class HistoricalIsCaseTpMarkedFeature
    implements HistoricalFeatureClientValuesRetriever<HistoricalFeatureInputDto> {

  private final HistoricalDecisionsQuery.Factory queryFactory;

  @Override
  public HistoricalFeatureInputDto retrieve(
      MatchData matchData, HistoricalDecisionsServiceClient historicalDecisionsServiceClient) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = queryFactory.create(matchData, historicalDecisionsServiceClient);
    var inputBuilder = HistoricalFeatureInputDto.builder();

    var response = query.getCaseTpMarkedInput().stream().findFirst();

    response.ifPresent(e -> inputBuilder
        .truePositiveCount(e.getTruePositivesCount())
        .modelKeyType(e.getModelKey().getModelKeyType())
    );

    var result = inputBuilder.feature(getFeatureName()).build();

    log.debug(
        "Datasource response for feature: {} with reason size {}.",
        result.getFeature(),
        result.getReason().size());

    return result;
  }

  @Override
  public Feature getFeature() {
    return Feature.HISTORICAL_IS_CASE_TP_MARKED;
  }
}
