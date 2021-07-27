package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalFeatureSolutionInputDto;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalInputResponse;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalSolutionInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class HistoricalInputProvider
    implements DataSourceInputProvider<HistoricalInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public HistoricalInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return HistoricalInputResponse.builder()
        .solutions(getInputs(matches, features))
        .build();
  }

  private List<HistoricalSolutionInputDto> getInputs(
      List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> HistoricalSolutionInputDto.builder()
            .match(match.getName())
            .features(getFeatureInputs(features, match.getMatchData()))
            .build())
        .collect(toList());
  }

  private List<HistoricalFeatureInputDto> getFeatureInputs(
      List<String> features, MatchData matchData) {
    // TODO: FeatureValuesRetriever
    return features.stream()
        .map(feature -> HistoricalFeatureInputDto.builder()
            .feature(feature)
            .featureSolutions(getSolutions())
            .build())
        .collect(Collectors.toList());
  }

  private List<HistoricalFeatureSolutionInputDto> getSolutions() {
    return List.of(HistoricalFeatureSolutionInputDto.builder()
        .solution("someSolution")
        .reason(Map.of("reason_key_1","reason_value_1"))
        .build());
  }

  @Override
  public List<Feature> getAllowedFeatures() {
    return List.of(
        Feature.HISTORICAL_IS_AP_TP_MARKED,
        Feature.HISTORICAL_IS_TP_MARKED,
        Feature.HISTORICAL_IS_CASE_TP_MARKED);
  }
}
