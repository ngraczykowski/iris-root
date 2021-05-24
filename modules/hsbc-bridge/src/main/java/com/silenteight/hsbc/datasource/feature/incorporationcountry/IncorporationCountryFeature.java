package com.silenteight.hsbc.datasource.feature.incorporationcountry;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class IncorporationCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {
    var apIncorporationCountries = customerEntitiesIncorporationCountries(
        matchData.getCustomerEntity());
    var mpIncorporationCountries = worldCheckEntitiesIncorporationCountries(
        matchData.getWorldCheckEntities());

    return CountryFeatureInputDto.builder()
        .feature(getFeatureName())
        .watchlistCountries(mpIncorporationCountries.distinct().collect(Collectors.toList()))
        .alertedPartyCountries(apIncorporationCountries.distinct().collect(Collectors.toList()))
        .build();
  }

  public static Stream<String> worldCheckEntitiesIncorporationCountries(
      List<WorldCheckEntity> worldCheckEntities) {
    return worldCheckEntities.stream()
        .flatMap(wce -> Stream.of(
            wce.getCountryCodesAll(),
            wce.getCountriesAll()
        ));
  }

  private static Stream<String> customerEntitiesIncorporationCountries(
      CustomerEntity customerEntity) {
    return Stream.of(
        customerEntity.getCountriesOfIncorporation(),
        customerEntity.getEdqIncorporationCountries(),
        customerEntity.getEdqIncorporationCountriesCodes()
    );
  }

  @Override
  public Feature getFeature() {
    return Feature.INCORPORATION_COUNTRY;
  }
}
