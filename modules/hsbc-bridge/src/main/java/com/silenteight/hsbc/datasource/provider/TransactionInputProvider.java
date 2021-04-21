package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.dto.transaction.TransactionFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.transaction.TransactionInputDto;
import com.silenteight.hsbc.datasource.dto.transaction.TransactionInputResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class TransactionInputProvider implements DataSourceInputProvider<TransactionInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public TransactionInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return TransactionInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  private List<TransactionInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> TransactionInputDto.builder()
            .match(match.getName())
            .featureInputs(getFeatureInputs(features, match.getRawData()))
            .build())
        .collect(Collectors.toList());
  }

  private List<TransactionFeatureInputDto> getFeatureInputs(
      List<String> features, MatchRawData matchRawData) {
    return features.stream()
        .map(featureName -> (TransactionFeatureInputDto)
            getFeatureRetriever(featureName).retrieve(matchRawData))
        .collect(Collectors.toList());
  }
}
