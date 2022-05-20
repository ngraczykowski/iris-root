package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckIndividualsCountryCodesExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  Stream<String> extract() {
    return worldCheckIndividuals.stream()
        .map(WorldCheckIndividual::getCountryCodesAll)
        .filter(StringUtils::isNotBlank)
        .flatMap(CountryCodesSplitter::splitCountryCodesBySpace)
        .filter(StringUtils::isNotBlank)
        .map(String::strip);
  }
}
