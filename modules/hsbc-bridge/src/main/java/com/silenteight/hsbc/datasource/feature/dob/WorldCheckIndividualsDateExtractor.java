package com.silenteight.hsbc.datasource.feature.dob;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckIndividualsDateExtractor implements Extractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  Stream<String> extract() {
    var mpDobsExtracted = worldCheckIndividuals
        .stream()
        .map(WorldCheckIndividual::getDobs)
        .filter(StringUtils::isNotBlank)
        .map(Object::toString)
        .map(s -> s.split("\\|"))
        .flatMap(Stream::of)
        .distinct();

    var mpYobsExtracted = worldCheckIndividuals
        .stream()
        .map(WorldCheckIndividual::getYearOfBirth)
        .filter(StringUtils::isNotBlank)
        .map(Object::toString)
        .map(s -> s.split(" "))
        .flatMap(Stream::of)
        .distinct();

    return result(mpDobsExtracted, mpYobsExtracted);
  }
}
