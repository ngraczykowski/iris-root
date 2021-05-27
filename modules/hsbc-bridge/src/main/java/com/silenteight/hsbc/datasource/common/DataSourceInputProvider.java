package com.silenteight.hsbc.datasource.common;

import lombok.NonNull;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureModel;
import com.silenteight.hsbc.datasource.feature.Retriever;
import com.silenteight.hsbc.datasource.provider.FeatureNotAllowedException;

import java.util.List;

import static java.util.stream.Collectors.toList;

public interface DataSourceInputProvider<R> {

  R toResponse(DataSourceInputCommand command);

  MatchFacade getMatchFacade();

  List<Feature> getAllowedFeatures();

  default R provideInput(@NonNull DataSourceInputRequest request) {
    validateFeatures(request.getFeatures());

    var matches = getMatchFacade().getMatches(request.getMatches());

    return toResponse(
        DataSourceInputCommand.builder()
            .matches(matches)
            .features(request.getFeatures())
            .build());
  }

  default Retriever getFeatureRetriever(String featureName) {
    return FeatureModel.getFeatureRetriever(featureName);
  }

  private void validateFeatures(List<String> features) {
    var allowedFeatures = getAllowedFeatures().stream()
        .map(Feature::getFullName)
        .collect(toList());

    features.forEach(feature -> {
      if (!allowedFeatures.contains(feature)) {
        throw new FeatureNotAllowedException(feature, allowedFeatures);
      }
    });
  }
}
