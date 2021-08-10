package com.silenteight.hsbc.datasource.feature.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalFeatureInputDto;
import com.silenteight.hsbc.datasource.extractors.historical.HistoricalDecisionsServiceClient;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.HistoricalFeatureClientValuesRetriever;

@RequiredArgsConstructor
public class HistoricalIsCaseTpMarkedFeature
    implements HistoricalFeatureClientValuesRetriever<HistoricalFeatureInputDto> {

  private final HistoricalDecisionsQuery.Factory queryFactory;

  @Override
  public HistoricalFeatureInputDto retrieve(
      MatchData matchData, HistoricalDecisionsServiceClient historicalDecisionsServiceClient) {
    var query = queryFactory.create(matchData, historicalDecisionsServiceClient);

    var inputBuilder = HistoricalFeatureInputDto.builder();

    var response = query.getCaseTpMarkedInput().stream().findFirst();

    response.ifPresent(e -> inputBuilder
        .truePositiveCount(e.getTruePositivesCount())
        .modelKeyType(e.getModelKey().getModelKeyType())
    );

    return inputBuilder.feature(getFeatureName()).build();
  }

  @Override
  public Feature getFeature() {
    return Feature.HISTORICAL_IS_CASE_TP_MARKED;
  }
}
