package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepInputRequest;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepInputResponse;
import com.silenteight.hsbc.datasource.extractors.ispep.IsPepServiceClient;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.IsPepFeatureClientValuesRetriever;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.datasource.feature.Feature.IS_PEP;
import static com.silenteight.hsbc.datasource.feature.FeatureModel.getFeatureRetriever;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class IsPepInputProvider {

  @Getter
  private final MatchFacade matchFacade;
  private final IsPepServiceClient isPepServiceClient;

  public List<IsPepInputResponse> provideInput(@NonNull IsPepInputRequest request) {
    var features = validateFeatures(request.getFeatures());
    var matches = matchFacade.getMatches(request.getMatches());

    return getInputs(matches, features);
  }

  private List<IsPepInputResponse> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> IsPepInputResponse.builder()
            .match(match.getName())
            .featureInputs(getFeatureInputs(
                features, match.getMatchData()))
            .build())
        .collect(Collectors.toList());
  }

  private List<IsPepFeatureInputDto> getFeatureInputs(
      List<String> features, MatchData matchData) {
    return features.stream()
        .map(featureName -> (IsPepFeatureInputDto)
            ((IsPepFeatureClientValuesRetriever) getFeatureRetriever(featureName))
                .retrieve(matchData, isPepServiceClient))
        .collect(Collectors.toList());
  }

  private List<String> validateFeatures(List<String> features) {
    var allowedFeatures = getAllowedFeatures().stream()
        .map(Feature::getFullName)
        .collect(toList());

    features.forEach(feature -> {
      if (!allowedFeatures.contains(feature)) {
        throw new FeatureNotAllowedException(feature, allowedFeatures);
      }
    });
    return features;
  }

  public List<Feature> getAllowedFeatures() {
    return of(IS_PEP);
  }
}
