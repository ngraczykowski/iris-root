package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CtrpScreeningEntitiesOtherCountriesExtractor {

  private final List<CtrpScreening> ctrpScreeningEntities;

  public Stream<String> extract() {
    return ctrpScreeningEntities
        .stream()
        .flatMap(
            CtrpScreeningEntitiesOtherCountriesExtractor::extractCtrpScreeningEntitiesOtherCountries);
  }

  private static Stream<String> extractCtrpScreeningEntitiesOtherCountries(
      CtrpScreening ctrpScreeningEntity) {
    return Stream.of(
        ctrpScreeningEntity.getCountryName(),
        ctrpScreeningEntity.getCountryCode(),
        ctrpScreeningEntity.getCtrpValue()
    ).filter(StringUtils::isNotBlank);
  }
}
