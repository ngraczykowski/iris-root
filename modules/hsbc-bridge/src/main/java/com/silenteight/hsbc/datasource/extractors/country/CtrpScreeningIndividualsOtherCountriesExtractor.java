package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CtrpScreeningIndividualsOtherCountriesExtractor {

  private final List<CtrpScreening> ctrpScreeningIndividuals;

  public Stream<String> extract() {
    return ctrpScreeningIndividuals
        .stream()
        .flatMap(
            CtrpScreeningIndividualsOtherCountriesExtractor::extractCtrpScreeningIndividualsOtherCountries);
  }

  private static Stream<String> extractCtrpScreeningIndividualsOtherCountries(
      CtrpScreening ctrpScreeningIndividual) {
    return of(
        ctrpScreeningIndividual.getCountryName(),
        ctrpScreeningIndividual.getCountryCode(),
        ctrpScreeningIndividual.getCtrpValue()
    );
  }
}
