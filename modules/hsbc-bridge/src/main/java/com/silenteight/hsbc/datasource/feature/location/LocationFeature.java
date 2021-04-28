package com.silenteight.hsbc.datasource.feature.location;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.dto.location.LocationFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class LocationFeature implements FeatureValuesRetriever<LocationFeatureInputDto> {

  private final LocationFeatureQuery.Factory queryFactory;

  @Override
  public LocationFeatureInputDto retrieve(MatchRawData matchRawData) {
    var query = queryFactory.create(matchRawData.getIndividualComposite());

    var alertedPartyResidencies = query.customerIndividualResidencies();
    var matchedPartyResidencies = query.worldCheckIndividualsResidencies();

    return LocationFeatureInputDto.builder()
        .alertedPartyLocations(alertedPartyResidencies.collect(toList()))
        .watchlistLocations(matchedPartyResidencies.collect(toList()))
        .feature(getFeature().getName())
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.LOCATION;
  }
}
