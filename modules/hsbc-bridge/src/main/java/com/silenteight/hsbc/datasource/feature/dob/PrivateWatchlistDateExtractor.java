package com.silenteight.hsbc.datasource.feature.dob;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class PrivateWatchlistDateExtractor {

  private static final int DETAILS_LENGTH_INDICATOR = 4;
  private final List<PrivateListIndividual> privateListIndividuals;

  Stream<String> extract() {
    var mpDobsExtracted = privateListIndividuals
        .stream()
        .map(PrivateListIndividual::getDateOfBirth)
        .filter(StringUtils::isNotBlank)
        .collect(toList());

    var mpYobsExtracted = privateListIndividuals
        .stream()
        .map(PrivateListIndividual::getYearOfBirth)
        .filter(StringUtils::isNotBlank)
        .map(Object::toString)
        .map(s -> s.split(" "))
        .flatMap(Stream::of)
        .collect(toList());

    var mpDobsHasDetails = mpDobsExtracted
        .stream()
        .map(String::length)
        .anyMatch(length -> length > DETAILS_LENGTH_INDICATOR);

    if (mpDobsHasDetails) {
      return mpDobsExtracted.stream();
    } else {
      return Stream.concat(mpDobsExtracted.stream(), mpYobsExtracted.stream());
    }
  }
}
