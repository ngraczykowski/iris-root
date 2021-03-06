package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

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
    return Stream.of(
        worldCheckEntity.getAddressCountry(),
        worldCheckEntity.getOperatingCountries(),
        worldCheckEntity.getCountryCodesAll(),
        worldCheckEntity.getCountriesAll(),
        worldCheckEntity.getNativeAliasLanguageCountry()
    ).filter(StringUtils::isNotBlank);
  }
}
