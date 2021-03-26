package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.country.CountryInputDto;
import com.silenteight.hsbc.datasource.dto.country.CountryInputResponse;
import com.silenteight.hsbc.datasource.dto.name.NameFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.name.NameInputDto;
import com.silenteight.hsbc.datasource.dto.name.NameInputResponse;

import java.util.List;
import java.util.stream.Collectors;

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
            .featureInputs(getFeatureInputs(features, match.getRawData()))
            .build())
        .collect(Collectors.toList());
  }

  private List<CountryFeatureInputDto> getFeatureInputs(
      List<String> features, MatchRawData matchRawData) {
    return features.stream()
        .map(featureName -> (CountryFeatureInputDto)
            getFeatureRetriever(featureName).retrieve(matchRawData))
        .collect(Collectors.toList());
  }
}
