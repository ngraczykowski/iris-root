package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.date.DateFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.date.DateInputDto;
import com.silenteight.hsbc.datasource.dto.date.DateInputResponse;
import com.silenteight.hsbc.datasource.feature.DateFeatureValuesRetriever;
import com.silenteight.hsbc.datasource.feature.Feature;

import java.util.List;
import java.util.Map;

import static com.silenteight.hsbc.datasource.feature.Feature.DATE_OF_BIRTH;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class DateInputProvider implements DataSourceInputProvider<DateInputResponse> {

  @Getter
  private final MatchFacade matchFacade;
  private final Map<String, List<String>> watchlistTypes;

  @Override
  public DateInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return DateInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  private List<DateInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> DateInputDto.builder()
            .match(match.getName())
            .featureInputs(getFeatureInputs(features, match.getMatchData()))
            .build())
        .collect(toList());
  }

  private List<DateFeatureInputDto> getFeatureInputs(List<String> features, MatchData matchData) {
    return features.stream()
        .map(featureName -> (DateFeatureInputDto)
            ((DateFeatureValuesRetriever) getFeatureRetriever(featureName)).retrieve(matchData, watchlistTypes))
        .collect(toList());
  }

  @Override
  public List<Feature> getAllowedFeatures() {
    return of(DATE_OF_BIRTH);
  }
}
