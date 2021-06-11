package com.silenteight.hsbc.datasource.feature.incorporationcountry;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity;
import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class IncorporationCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {
    var builder = CountryFeatureInputDto.builder()
        .feature(getFeatureName());

    if (matchData.isEntity()) {
      var apIncorporationCountries = customerEntitiesIncorporationCountries(
          matchData.getCustomerEntity());
      var mpIncorporationCountries = worldCheckEntitiesIncorporationCountries(
          matchData.getWorldCheckEntities());

      builder.watchlistCountries(mpIncorporationCountries.distinct().collect(toList()));
      builder.alertedPartyCountries(apIncorporationCountries.distinct().collect(toList()));
    } else {
      builder.watchlistCountries(emptyList());
      builder.alertedPartyCountries(emptyList());
    }

    return builder.build();
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
