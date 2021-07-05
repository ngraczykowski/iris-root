package com.silenteight.hsbc.datasource.feature.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.location.LocationFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static com.silenteight.hsbc.datasource.feature.Feature.GEO_PLACE_OF_BIRTH;

@RequiredArgsConstructor
public class GeoPlaceOfBirthFeature implements FeatureValuesRetriever<LocationFeatureInputDto> {

  private final GeoPlaceOfBirthFeatureQuery.Factory queryFactory;

  @Override
  public LocationFeatureInputDto retrieve(MatchData matchData) {
    var query = queryFactory.create(matchData);
    var inputBuilder = LocationFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyLocation(query.getApGeoPlacesOfBirth());
      inputBuilder.watchlistLocation(query.getMpGeoPlaceOfBirth());
    } else {
      inputBuilder.alertedPartyLocation("");
      inputBuilder.watchlistLocation("");
    }

    return inputBuilder
        .feature(getFeatureName())
        .build();
  }

  @Override
  public Feature getFeature() {
    return GEO_PLACE_OF_BIRTH;
  }
}
