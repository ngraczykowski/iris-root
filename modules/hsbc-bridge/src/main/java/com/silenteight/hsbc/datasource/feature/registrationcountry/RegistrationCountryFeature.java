package com.silenteight.hsbc.datasource.feature.registrationcountry;

import com.silenteight.hsbc.bridge.domain.CustomerEntities;
import com.silenteight.hsbc.bridge.domain.WorldCheckEntities;
import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class RegistrationCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  @Override
  public CountryFeatureInputDto retrieve(MatchRawData matchRawData) {
    var mpRegistrationCountries =
        worldCheckEntityCountries(matchRawData.getEntityComposite().getWorldCheckEntities());
    var apRegistrationCountries = customerEntityRegistrationCountries(
        matchRawData.getEntityComposite().getCustomerEntities());

    return CountryFeatureInputDto.builder()
        .feature(getFeatureName())
        .alertedPartyCountries(apRegistrationCountries.distinct().collect(toList()))
        .watchlistCountries(mpRegistrationCountries.distinct().collect(toList()))
        .build();
  }

  private static Stream<String> customerEntityRegistrationCountries(
      CustomerEntities customerEntities) {
    return Stream.of(
        customerEntities.getRegistrationCountry(),
        customerEntities.getCountriesOfRegistrationOriginal(),
        customerEntities.getEdqRegiistrationCountriesCodes()
    );
  }

  private static Stream<String> worldCheckEntityCountries(
      List<WorldCheckEntities> worldCheckEntities) {
    return worldCheckEntities.stream().map(WorldCheckEntities::getRegistrationCountry);
  }

  @Override
  public Feature getFeature() {
    return Feature.REGISTRATION_COUNTRY;
  }
}
