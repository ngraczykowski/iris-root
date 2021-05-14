package com.silenteight.hsbc.datasource.feature.dob;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckDateExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  Stream<String> extract() {
    var mpDobs = worldCheckIndividuals
        .stream()
        .map(WorldCheckIndividual::getDobs)
        .filter(StringUtils::isNotBlank);

    var mpYobs = worldCheckIndividuals
        .stream()
        .map(WorldCheckIndividual::getYearOfBirth)
        .filter(StringUtils::isNotBlank)
        .map(Object::toString);

    return Stream.concat(mpDobs, mpYobs);
  }
}
