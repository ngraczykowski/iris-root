package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.gender.GenderFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.gender.GenderInputDto;
import com.silenteight.hsbc.datasource.dto.gender.GenderInputResponse;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.datasource.feature.Feature.GENDER;
import static java.util.List.of;

@RequiredArgsConstructor
class GenderInputProvider implements DataSourceInputProvider<GenderInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public GenderInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return GenderInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  private List<GenderInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> GenderInputDto.builder()
            .match(match.getName())
            .featureInputs(getFeatureInputs(features, match.getMatchData()))
            .build())
        .collect(Collectors.toList());
  }

  private List<GenderFeatureInputDto> getFeatureInputs(
      List<String> features, MatchData matchData) {
    return features.stream()
        .map(featureName -> (GenderFeatureInputDto)
            ((FeatureValuesRetriever) getFeatureRetriever(featureName)).retrieve(matchData))
        .collect(Collectors.toList());
  }

  @Override
  public List<Feature> getAllowedFeatures() {
    return of(GENDER);
  }
}
