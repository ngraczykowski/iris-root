package com.silenteight.hsbc.datasource.common;

import lombok.NonNull;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.feature.FeatureModel;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

public interface DataSourceInputProvider<R> {

  default R provideInput(@NonNull DataSourceInputRequest request) {
    var matches = getMatchFacade().getMatches(request.getMatches());

    return toResponse(
        DataSourceInputCommand.builder()
            .matches(matches)
            .features(request.getFeatures())
            .build());
  }

  R toResponse(DataSourceInputCommand command);

  default FeatureValuesRetriever getFeatureRetriever(String featureName) {
    return FeatureModel.getFeatureRetriever(featureName);
  }

  MatchFacade getMatchFacade();
}
