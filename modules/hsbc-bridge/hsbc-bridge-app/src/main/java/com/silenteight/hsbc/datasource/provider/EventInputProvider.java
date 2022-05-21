package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.event.EventFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.event.EventInputDto;
import com.silenteight.hsbc.datasource.dto.event.EventInputResponse;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class EventInputProvider implements DataSourceInputProvider<EventInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public EventInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return EventInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  private List<EventInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> EventInputDto.builder()
            .match(match.getName())
            .featureInputs(getFeatureInputs(features, match.getMatchData()))
            .build())
        .collect(Collectors.toList());
  }

  private List<EventFeatureInputDto> getFeatureInputs(
      List<String> features, MatchData matchData) {
    return features.stream()
        .map(featureName -> (EventFeatureInputDto)
            ((FeatureValuesRetriever) getFeatureRetriever(featureName)).retrieve(matchData))
        .collect(Collectors.toList());
  }

  @Override
  public List<Feature> getAllowedFeatures() {
    return List.of(/*Implement with feature*/);
  }
}
