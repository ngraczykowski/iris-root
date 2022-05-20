package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class PrivateListIndividualsOtherCountriesExtractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  public Stream<String> extract() {
    return privateListIndividuals
        .stream()
        .flatMap(
            PrivateListIndividualsOtherCountriesExtractor::extractPrivateListIndividualsOtherCountries);
  }

  private static Stream<String> extractPrivateListIndividualsOtherCountries(
      PrivateListIndividual privateListIndividual) {
    return Stream.of(
        privateListIndividual.getCountryCodesAll(),
        privateListIndividual.getCountriesAll()
    ).filter(StringUtils::isNotBlank);
  }
}
