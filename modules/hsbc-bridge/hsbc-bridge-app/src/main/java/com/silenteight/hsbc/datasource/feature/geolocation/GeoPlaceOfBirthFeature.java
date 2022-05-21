package com.silenteight.hsbc.datasource.feature.geolocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.location.LocationFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

@Slf4j
@RequiredArgsConstructor
public class GeoPlaceOfBirthFeature implements FeatureValuesRetriever<LocationFeatureInputDto> {

  private final GeoPlaceOfBirthFeatureQuery.Factory queryFactory;

  @Override
  public LocationFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = queryFactory.create(matchData);
    var inputBuilder = LocationFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyLocation(query.getApGeoPlacesOfBirth());
      inputBuilder.watchlistLocation(query.getMpGeoPlaceOfBirth());
    } else {
      inputBuilder.alertedPartyLocation("");
      inputBuilder.watchlistLocation("");
    }

    var result = inputBuilder
        .feature(getFeatureName())
        .build();

    log.debug(
        "Datasource response for feature: {} with alerted party {} and watchlist party {}.",
        result.getFeature(),
        result.getAlertedPartyLocation(),
        result.getWatchlistLocation());

    return result;
  }

  @Override
  public Feature getFeature() {
    return Feature.GEO_PLACE_OF_BIRTH;
  }
}
