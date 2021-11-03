package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchComposite;
import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.location.LocationFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.location.LocationInputDto;
import com.silenteight.hsbc.datasource.dto.location.LocationInputResponse;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureModel;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;
import com.silenteight.hsbc.datasource.feature.LocationFeatureClientValuesRetriever;
import com.silenteight.hsbc.datasource.feature.country.CountryDiscoverer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.feature.Feature.GEO_PLACE_OF_BIRTH;
import static com.silenteight.hsbc.datasource.feature.Feature.GEO_RESIDENCIES;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class LocationInputProvider implements DataSourceInputProvider<LocationInputResponse> {

  @Getter
  private final MatchFacade matchFacade;
  private final CountryDiscoverer countryDiscoverer;
  private final NameInformationServiceClient nameInformationServiceClient;

  @Override
  public LocationInputResponse toResponse(DataSourceInputCommand command) {
    var features = command.getFeatures();
    var matches = command.getMatches();

    return LocationInputResponse.builder()
        .inputs(getInputs(matches, features))
        .build();
  }

  private List<LocationInputDto> getInputs(List<MatchComposite> matches, List<String> features) {
    return matches.stream()
        .map(match -> LocationInputDto.builder()
            .match(match.getName())
            .featureInputs(getFeatureInputs(features, match.getMatchData()))
            .build())
        .collect(toList());
  }

  private List<LocationFeatureInputDto> getFeatureInputs(List<String> features, MatchData matchData) {
    return features.stream()
        .map(feature -> this.retrieve(feature, matchData))
        .collect(toList());
  }

  private LocationFeatureInputDto retrieve(String feature, MatchData matchData) {
    if (GEO_PLACE_OF_BIRTH.getFullName().equals(feature)) {
      return (LocationFeatureInputDto) ((FeatureValuesRetriever) FeatureModel.getFeatureRetriever(feature)).retrieve(matchData);
    }
    if (GEO_RESIDENCIES.getFullName().equals(feature)) {
      return ((LocationFeatureClientValuesRetriever) FeatureModel.getFeatureRetriever(feature)).retrieve(matchData, countryDiscoverer, nameInformationServiceClient);
    }
    throw new FeatureNotAllowedException(feature, Stream.of(GEO_PLACE_OF_BIRTH, GEO_RESIDENCIES).map(Feature::getFullName).collect(Collectors.toList()));
  }

  @Override
  public List<Feature> getAllowedFeatures() {
    return of(GEO_PLACE_OF_BIRTH, GEO_RESIDENCIES);
  }
}
