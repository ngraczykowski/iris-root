package com.silenteight.hsbc.datasource.feature.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.location.LocationFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static com.silenteight.hsbc.datasource.feature.Feature.GEO_RESIDENCIES;

@RequiredArgsConstructor
public class GeoResidencyFeature implements FeatureValuesRetriever<LocationFeatureInputDto> {

  private final GeoResidencyFeatureQuery.Factory queryFactory;

  @Override
  public LocationFeatureInputDto retrieve(MatchData matchData) {
    var query = queryFactory.create(matchData);
    var inputBuilder = LocationFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyLocation(query.getApIndividualsGeoResidencies());
      inputBuilder.watchlistLocation(query.getMpIndividualsGeoResidencies());
    } else {
      inputBuilder.alertedPartyLocation(query.getApEntitiesGeoResidencies());
      inputBuilder.watchlistLocation(query.getMpEntitiesGeoResidencies());
    }

    return inputBuilder
        .feature(getFeatureName())
        .build();
  }

  @Override
  public Feature getFeature() {
    return GEO_RESIDENCIES;
  }
}
