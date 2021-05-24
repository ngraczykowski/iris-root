package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class WorldCheckEntitiesOtherCountriesExtractor {

  private final List<WorldCheckEntity> worldCheckEntities;

  public Stream<String> extract() {
    return worldCheckEntities
        .stream()
        .flatMap(
            WorldCheckEntitiesOtherCountriesExtractor::extractWorldCheckEntitiesOtherCountries);
  }

  private static Stream<String> extractWorldCheckEntitiesOtherCountries(
      WorldCheckEntity worldCheckEntity) {
    return of(
        worldCheckEntity.getAddressCountry(),
        worldCheckEntity.getOperatingCountries(),
        worldCheckEntity.getCountryCodesAll(),
        worldCheckEntity.getCountriesAll(),
        worldCheckEntity.getNativeAliasLanguageCountry()
    );
  }
}
