package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdInputDto;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdInputResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class NationalIdInputProvider implements DataSourceInputProvider<NationalIdInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public NationalIdInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return NationalIdInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  private List<NationalIdInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> NationalIdInputDto.builder()
            .match(match.getName())
            .featureInputs(getFeatureInputs(features, match.getRawData()))
            .build())
        .collect(Collectors.toList());
  }

  private List<NationalIdFeatureInputDto> getFeatureInputs(
      List<String> features, MatchRawData matchRawData) {
    return features.stream()
        .map(featureName -> (NationalIdFeatureInputDto)
            getFeatureRetriever(featureName).retrieve(matchRawData))
        .collect(Collectors.toList());
  }
}
