package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;
import com.silenteight.hsbc.datasource.extractors.common.SignType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckIndividualsPlaceOfBirthExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  public List<String> extract() {
    return worldCheckIndividuals.stream()
        .flatMap(WorldCheckIndividualsPlaceOfBirthExtractor::extractWorldCheckFields)
        .collect(Collectors.toList());
  }

  private static Stream<String> extractWorldCheckFields(WorldCheckIndividual worldCheckIndividual) {
    return GeoLocationExtractor.splitExtractedValueBySign(
        SignType.SEMICOLON, worldCheckIndividual.getPlaceOfBirthOriginal());
  }
}
