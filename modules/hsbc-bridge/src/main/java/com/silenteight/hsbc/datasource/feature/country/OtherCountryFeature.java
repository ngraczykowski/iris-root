package com.silenteight.hsbc.datasource.feature.country;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;
import com.silenteight.hsbc.datasource.util.StreamUtils;

@Slf4j
@RequiredArgsConstructor
public class OtherCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  private final OtherCountryQuery.Factory queryFactory;

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = queryFactory.create(matchData);
    var inputBuilder = CountryFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyCountries(StreamUtils.toDistinctList(
          query.apCustomerIndividualOtherCountries()));
      inputBuilder.watchlistCountries(StreamUtils.toDistinctList(
          query.mpWorldCheckIndividualsOtherCountries(),
          query.mpPrivateListIndividualsOtherCountries(),
          query.mpCtrpScreeningIndividualsOtherCountries()));
    } else {
      inputBuilder.alertedPartyCountries(StreamUtils.toDistinctList(
          query.apCustomerEntityOtherCountries()));
      inputBuilder.watchlistCountries(StreamUtils.toDistinctList(
          query.mpWorldCheckEntitiesOtherCountries(),
          query.mpPrivateListEntitiesOtherCountries(),
          query.mpCtrpScreeningEntitiesOtherCountries()));
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

  @Override
  public Feature getFeature() {
    return Feature.OTHER_COUNTRY;
  }
}
