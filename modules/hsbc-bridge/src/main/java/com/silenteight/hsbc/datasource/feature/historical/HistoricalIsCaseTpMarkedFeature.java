package com.silenteight.hsbc.datasource.feature.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalFeatureSolutionInputDto;
import com.silenteight.hsbc.datasource.extractors.historical.HistoricalDecisionsServiceClient;
import com.silenteight.hsbc.datasource.extractors.historical.ModelCountsDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.HistoricalFeatureClientValuesRetriever;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HistoricalIsCaseTpMarkedFeature
    implements HistoricalFeatureClientValuesRetriever<HistoricalFeatureInputDto> {

  private final HistoricalDecisionsQuery.Factory queryFactory;

  @Override
  public HistoricalFeatureInputDto retrieve(
      MatchData matchData, HistoricalDecisionsServiceClient historicalDecisionsServiceClient) {
    var query = queryFactory.create(matchData, historicalDecisionsServiceClient);

    var inputBuilder = HistoricalFeatureInputDto.builder();

    var responses = query.getCaseTpMarkedSolution();

    return inputBuilder
        .featureSolutions(mapToFeatureSolutions(responses))
        .feature(getFeatureName())
        .build();
  }

  private List<HistoricalFeatureSolutionInputDto> mapToFeatureSolutions(
      List<ModelCountsDto> responses) {
    return responses.stream()
        .map(response -> HistoricalFeatureSolutionInputDto.builder()
            .solution(String.valueOf(response.getTruePositivesCount()))
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public Feature getFeature() {
    return Feature.HISTORICAL_IS_CASE_TP_MARKED;
  }
}
