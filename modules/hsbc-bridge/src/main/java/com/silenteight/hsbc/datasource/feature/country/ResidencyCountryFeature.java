package com.silenteight.hsbc.datasource.feature.country;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class ResidencyCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  private final ResidencyCountryFeatureQuery.Factory queryFactory;

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = queryFactory.create(matchData);
    var inputBuilder = CountryFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyCountries(getDistinctValues(query.customerIndividualResidencies()));
      inputBuilder.watchlistCountries(getDistinctValues(query.worldCheckIndividualsResidencies()));
    } else {
      inputBuilder.alertedPartyCountries(List.of());
      inputBuilder.watchlistCountries(List.of());
    }

    var result = inputBuilder
        .feature(getFeatureName())
        .build();

    log.debug(
        "Datasource response for feature: {} with alerted party size {} and watchlist party size {}.",
        result.getFeature(),
        result.getAlertedPartyCountries().size(),
        result.getWatchlistCountries().size());

    return result;
  }

  private List<String> getDistinctValues(Stream<String> stream) {
    return stream.distinct().collect(toList());
  }

  @Override
  public Feature getFeature() {
    return Feature.RESIDENCY_COUNTRY;
  }
}
