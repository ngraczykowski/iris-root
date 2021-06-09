package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.name.NameFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.name.NameInputDto;
import com.silenteight.hsbc.datasource.dto.name.NameInputResponse;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureClientValuesRetriever;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.datasource.feature.Feature.NAME;
import static java.util.List.of;

@RequiredArgsConstructor
class NameInputProvider implements DataSourceInputProvider<NameInputResponse> {

  @Getter
  private final MatchFacade matchFacade;
  private final NameInformationServiceClient nameInformationServiceClient;

  @Override
  public NameInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return NameInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  private List<NameInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> NameInputDto.builder()
            .match(match.getName())
            .featureInputs(getFeatureInputs(features, match.getMatchData()))
            .build())
        .collect(Collectors.toList());
  }

  private List<NameFeatureInputDto> getFeatureInputs(List<String> features, MatchData matchData) {
    return features.stream()
        .map(featureName -> (NameFeatureInputDto)
            ((FeatureClientValuesRetriever) getFeatureRetriever(featureName))
                .retrieve(matchData, nameInformationServiceClient))
        .collect(Collectors.toList());
  }

  @Override
  public List<Feature> getAllowedFeatures() {
    return of(NAME);
  }
}
