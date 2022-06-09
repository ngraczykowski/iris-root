package com.silenteight.hsbc.datasource.feature.country;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;
import com.silenteight.hsbc.datasource.util.StreamUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
      inputBuilder.watchlistCountries(StreamUtils.toDistinctList(
          query.worldCheckIndividualsResidencies(),
          query.privateListIndividualsResidencies(),
          query.ctrpScreeningResidencies(),
          query.nnsIndividualsResidencies()));
    } else {
      inputBuilder.alertedPartyCountries(Collections.emptyList());
      inputBuilder.watchlistCountries(Collections.emptyList());
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
    return stream.distinct().collect(Collectors.toList());
  }

  @Override
  public Feature getFeature() {
    return Feature.RESIDENCY_COUNTRY;
  }
}
