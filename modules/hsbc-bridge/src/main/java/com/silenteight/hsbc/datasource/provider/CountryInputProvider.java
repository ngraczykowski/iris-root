package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.country.CountryInputDto;
import com.silenteight.hsbc.datasource.dto.country.CountryInputResponse;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.datasource.feature.Feature.*;
import static java.util.List.of;

@RequiredArgsConstructor
class CountryInputProvider implements DataSourceInputProvider<CountryInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public CountryInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return CountryInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  private List<CountryInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> CountryInputDto.builder()
            .match(match.getName())
            .featureInputs(getFeatureInputs(features, match.getMatchData()))
            .build())
        .collect(Collectors.toList());
  }

  private List<CountryFeatureInputDto> getFeatureInputs(
      List<String> features, MatchData matchData) {
    return features.stream()
        .map(featureName -> (CountryFeatureInputDto)
            ((FeatureValuesRetriever) getFeatureRetriever(featureName)).retrieve(matchData))
        .collect(Collectors.toList());
  }

  @Override
  public List<Feature> getAllowedFeatures() {
    return of(
        NATIONALITY_COUNTRY,
        OTHER_COUNTRY,
        RESIDENCY_COUNTRY,
        INCORPORATION_COUNTRY,
        REGISTRATION_COUNTRY);
  }
}
