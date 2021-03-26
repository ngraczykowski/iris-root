package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.dto.document.DocumentFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.document.DocumentInputDto;
import com.silenteight.hsbc.datasource.dto.document.DocumentInputResponse;
import com.silenteight.hsbc.datasource.dto.name.NameFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.name.NameInputDto;
import com.silenteight.hsbc.datasource.dto.name.NameInputResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class DocumentInputProvider implements DataSourceInputProvider<DocumentInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public DocumentInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return DocumentInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  private List<DocumentInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> DocumentInputDto.builder()
            .match(match.getName())
            .featureInputs(getFeatureInputs(features, match.getRawData()))
            .build())
        .collect(Collectors.toList());
  }

  private List<DocumentFeatureInputDto> getFeatureInputs(
      List<String> features, MatchRawData matchRawData) {
    return features.stream()
        .map(featureName -> (DocumentFeatureInputDto)
            getFeatureRetriever(featureName).retrieve(matchRawData))
        .collect(Collectors.toList());
  }
}
