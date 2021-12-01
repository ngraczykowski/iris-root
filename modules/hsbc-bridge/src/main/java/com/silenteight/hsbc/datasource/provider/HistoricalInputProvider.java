package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalDecisionsFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalDecisionsInputDto;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalInputResponse;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class HistoricalInputProvider implements DataSourceInputProvider<HistoricalInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public HistoricalInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return HistoricalInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  private List<HistoricalDecisionsInputDto> getInputs(
      List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> HistoricalDecisionsInputDto.builder()
            .match(match.getName())
            .features(getFeatureInputs(features, match.getMatchData()))
            .build())
        .collect(Collectors.toList());
  }

  private List<HistoricalDecisionsFeatureInputDto> getFeatureInputs(
      List<String> features, MatchData matchData) {
    return features.stream()
        .map(featureName -> (HistoricalDecisionsFeatureInputDto)
            ((FeatureValuesRetriever) getFeatureRetriever(featureName)).retrieve(matchData))
        .collect(Collectors.toList());
  }

  @Override
  public List<Feature> getAllowedFeatures() {
    return List.of(
        Feature.HISTORICAL_IS_AP_TP_MARKED,
        Feature.HISTORICAL_IS_TP_MARKED,
        Feature.HISTORICAL_IS_CASE_TP_MARKED);
  }
}
