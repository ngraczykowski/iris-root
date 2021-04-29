package com.silenteight.hsbc.datasource.feature.incorporationcountry;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.domain.CustomerEntities;
import com.silenteight.hsbc.bridge.domain.WorldCheckEntities;
import com.silenteight.hsbc.bridge.match.MatchRawData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class IncorporationCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  @Override
  public CountryFeatureInputDto retrieve(MatchRawData matchRawData) {
    var apIncorporationCountries = customerEntitiesIncorporationCountries(
        matchRawData.getEntityComposite().getCustomerEntities());
    var mpIncorporationCountries = worldCheckEntitiesIncorporationCountries(
        matchRawData.getEntityComposite().getWorldCheckEntities());

    return CountryFeatureInputDto.builder()
        .feature(getFeatureName())
        .watchlistCountries(mpIncorporationCountries.distinct().collect(Collectors.toList()))
        .alertedPartyCountries(apIncorporationCountries.distinct().collect(Collectors.toList()))
        .build();
  }

  private static Stream<String> worldCheckEntitiesIncorporationCountries(
      List<WorldCheckEntities> worldCheckEntities) {
    return worldCheckEntities.stream()
        .flatMap(wce -> Stream.of(
            wce.getCountryCodesAll(),
            wce.getCountriesAll()
        ));
  }

  private static Stream<String> customerEntitiesIncorporationCountries(
      CustomerEntities customerEntities) {
    return Stream.of(
        customerEntities.getCountriesOfIncorporation(),
        customerEntities.getEdqIncorporationCountries(),
        customerEntities.getEdqIncorporationCountriesCodes()
    );
  }

  @Override
  public Feature getFeature() {
    return Feature.INCORPORATION_COUNTRY;
  }
}
