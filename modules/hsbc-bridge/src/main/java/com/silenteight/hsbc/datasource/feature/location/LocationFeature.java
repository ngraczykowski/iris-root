package com.silenteight.hsbc.datasource.feature.location;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class LocationFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  private final LocationFeatureQuery.Factory queryFactory;

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {
    var query = queryFactory.create(matchData);

    var alertedPartyResidencies = query.customerIndividualResidencies().distinct();
    var matchedPartyResidencies = query.worldCheckIndividualsResidencies().distinct();

    return CountryFeatureInputDto.builder()
        .alertedPartyCountries(alertedPartyResidencies.collect(toList()))
        .watchlistCountries(matchedPartyResidencies.collect(toList()))
        .feature(getFeature().getName())
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.LOCATION;
  }
}
