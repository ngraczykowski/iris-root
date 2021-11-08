package com.silenteight.hsbc.datasource.feature.country;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;
import com.silenteight.hsbc.datasource.util.StreamUtils;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class RegistrationCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  private final RegistrationCountryFeatureQuery.Factory queryFactory;

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = queryFactory.create(matchData);
    var builder = CountryFeatureInputDto.builder();

    if (matchData.isEntity()) {
      var apRegistrationCountries = query.getCustomerEntityRegistrationCountries();
      var wlRegistrationCountries = query.getWatchlistEntityRegistrationCountries();
      builder.alertedPartyCountries(StreamUtils.toDistinctList(apRegistrationCountries));
      builder.watchlistCountries(StreamUtils.toDistinctList(wlRegistrationCountries));
    }

    var result = builder
        .feature(getFeatureName())
        .build();

    log.debug(
        "Datasource response for feature: {} with alerted party size {} and watchlist party size {}.",
        result.getFeature(),
        result.getAlertedPartyCountries().size(),
        result.getWatchlistCountries().size());

    return result;
  }

  public static Stream<String> worldCheckEntityCountries(
      List<WorldCheckEntity> worldCheckEntities) {
    return worldCheckEntities.stream().map(WorldCheckEntity::getRegistrationCountry);
  }

  @Override
  public Feature getFeature() {
    return Feature.REGISTRATION_COUNTRY;
  }
}
