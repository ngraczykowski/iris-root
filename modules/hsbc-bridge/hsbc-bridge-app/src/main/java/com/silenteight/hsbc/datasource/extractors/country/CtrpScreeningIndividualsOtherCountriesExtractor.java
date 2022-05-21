package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

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
    return Stream.of(
        ctrpScreeningIndividual.getCountryName(),
        ctrpScreeningIndividual.getCountryCode(),
        ctrpScreeningIndividual.getCtrpValue()
    ).filter(StringUtils::isNotBlank);
  }
}
