package com.silenteight.hsbc.datasource.feature.registrationcountry;

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class RegistrationCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {
    var mpRegistrationCountries =
        worldCheckEntityCountries(matchData.getWorldCheckEntities());
    var apRegistrationCountries = customerEntityRegistrationCountries(
        matchData.getCustomerEntity());

    return CountryFeatureInputDto.builder()
        .feature(getFeatureName())
        .alertedPartyCountries(apRegistrationCountries.distinct().collect(toList()))
        .watchlistCountries(mpRegistrationCountries.distinct().collect(toList()))
        .build();
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
