package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepInputDto;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepInputRequest;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureModel;
import com.silenteight.hsbc.datasource.feature.IsPepFeatureValuesRetriever;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class IsPepInputProvider {

  @Getter
  private final MatchFacade matchFacade;

  public List<IsPepInputDto> provideInput(@NonNull IsPepInputRequest request) {
    var features = validateFeatures(request.getFeatures());
    var matches = matchFacade.getMatches(request.getMatches());

    return getInputs(matches, features);
  }

  private List<IsPepInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .flatMap(match -> getIsPepInputs(features, match))
        .collect(Collectors.toList());
  }

  private Stream<IsPepInputDto> getIsPepInputs(List<String> features, MatchComposite match) {
    return features.stream()
        .map(featureName -> (IsPepInputDto)
            ((IsPepFeatureValuesRetriever) FeatureModel.getFeatureRetriever(featureName)).retrieve(
                match.getMatchData(), match.getName()));
  }

  private List<String> validateFeatures(List<String> features) {
    var allowedFeatures = getAllowedFeatures().stream()
        .map(Feature::getFullName)
        .collect(Collectors.toList());

    features.forEach(feature -> {
      if (!allowedFeatures.contains(feature)) {
        throw new FeatureNotAllowedException(feature, allowedFeatures);
      }
    });
    return features;
  }

  public List<Feature> getAllowedFeatures() {
    return List.of(Feature.IS_PEP);
  }
}
