package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.SignType.SEMICOLON;
import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.splitExtractedValueBySign;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class WorldCheckIndividualsPlaceOfBirthExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  public List<String> extract() {
    return worldCheckIndividuals.stream()
        .flatMap(WorldCheckIndividualsPlaceOfBirthExtractor::extractWorldCheckFields)
        .collect(toList());
  }

  private static Stream<String> extractWorldCheckFields(WorldCheckIndividual worldCheckIndividual) {
    return splitExtractedValueBySign(SEMICOLON, worldCheckIndividual.getPlaceOfBirthOriginal());
  }
}
