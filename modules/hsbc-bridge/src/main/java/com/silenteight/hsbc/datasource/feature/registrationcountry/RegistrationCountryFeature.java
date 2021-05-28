package com.silenteight.hsbc.datasource.feature.registrationcountry;

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;

public class RegistrationCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {
    var builder = CountryFeatureInputDto.builder()
        .feature(getFeatureName());

    if (matchData.isEntity()) {
      var customerEntity = matchData.getCustomerEntity();

      var wlRegistrationCountries = customerEntityRegistrationCountries(
          customerEntity);
      builder.watchlistCountries(toDistinctList(wlRegistrationCountries));

      var apRegistrationCountries = customerEntityRegistrationCountries(
          customerEntity);
      builder.alertedPartyCountries(toDistinctList(apRegistrationCountries));
    }

    return builder.build();
  }

  public static Stream<String> worldCheckEntityCountries(
      List<WorldCheckEntity> worldCheckEntities) {
    return worldCheckEntities.stream().map(WorldCheckEntity::getRegistrationCountry);
  }

  private static Stream<String> customerEntityRegistrationCountries(CustomerEntity customerEntity) {
    return Stream.of(
        customerEntity.getRegistrationCountry(),
        customerEntity.getCountriesOfRegistrationOriginal(),
        customerEntity.getEdqRegistrationCountriesCodes()
    );
  }

  @Override
  public Feature getFeature() {
    return Feature.REGISTRATION_COUNTRY;
  }
}
