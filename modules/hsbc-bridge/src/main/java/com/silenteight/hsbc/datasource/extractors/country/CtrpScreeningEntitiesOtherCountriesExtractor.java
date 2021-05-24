package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;

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
    return of(
        ctrpScreeningEntity.getCountryName(),
        ctrpScreeningEntity.getCountryCode()
    );
  }
}
