package com.silenteight.hsbc.datasource.feature.geolocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.location.LocationFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static com.silenteight.hsbc.datasource.feature.Feature.GEO_RESIDENCIES;

@Slf4j
@RequiredArgsConstructor
public class GeoResidencyFeature implements FeatureValuesRetriever<LocationFeatureInputDto> {

  private final GeoResidencyFeatureQuery.Factory queryFactory;

  @Override
  public LocationFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = queryFactory.create(matchData);
    var inputBuilder = LocationFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyLocation(query.getApIndividualsGeoResidencies());
      inputBuilder.watchlistLocation(query.getMpIndividualsGeoResidencies());
    } else {
      inputBuilder.alertedPartyLocation(query.getApEntitiesGeoResidencies());
      inputBuilder.watchlistLocation(query.getMpEntitiesGeoResidencies());
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
    return GEO_RESIDENCIES;
  }
}
