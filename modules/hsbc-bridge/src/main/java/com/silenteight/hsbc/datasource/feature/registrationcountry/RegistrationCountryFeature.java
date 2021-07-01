package com.silenteight.hsbc.datasource.feature.registrationcountry;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;
import com.silenteight.hsbc.datasource.feature.country.RegistrationCountryFeatureQuery;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;

@RequiredArgsConstructor
public class RegistrationCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  private final RegistrationCountryFeatureQuery.Factory queryFactory;

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {
    var query = queryFactory.create(matchData);
    var builder = CountryFeatureInputDto.builder();

    if (matchData.isEntity()) {
      var apRegistrationCountries = query.getCustomerEntityRegistrationCountries();
      var wlRegistrationCountries = query.getWatchlistEntityRegistrationCountries();
      builder.alertedPartyCountries(toDistinctList(apRegistrationCountries));
      builder.watchlistCountries(toDistinctList(wlRegistrationCountries));
    }

    return builder
        .feature(getFeatureName())
        .build();
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
