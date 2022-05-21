package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListEntity;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class PrivateListEntitiesOtherCountriesExtractor {

  private final List<PrivateListEntity> privateListEntities;

  public Stream<String> extract() {
    return privateListEntities
        .stream()
        .flatMap(
            PrivateListEntitiesOtherCountriesExtractor::extractPrivateListEntitiesOtherCountries);
  }

  private static Stream<String> extractPrivateListEntitiesOtherCountries(
      PrivateListEntity privateListEntity) {
    return Stream.of(
        privateListEntity.getAddressCountry(),
        privateListEntity.getOperatingCountries(),
        privateListEntity.getCountryCodesAll(),
        privateListEntity.getCountriesAll()
    ).filter(StringUtils::isNotBlank);
  }
}
