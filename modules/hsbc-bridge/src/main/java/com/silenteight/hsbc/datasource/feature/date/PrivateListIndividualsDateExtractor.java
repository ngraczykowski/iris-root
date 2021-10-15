package com.silenteight.hsbc.datasource.feature.date;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.PrivateListIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class PrivateListIndividualsDateExtractor implements Extractor {

  private final List<PrivateListIndividual> privateListIndividuals;

  Stream<String> extract() {
    var mpDobsExtracted = privateListIndividuals
        .stream()
        .map(PrivateListIndividual::getDateOfBirth)
        .filter(StringUtils::isNotBlank)
        .distinct();

    var mpYobsExtracted = privateListIndividuals
        .stream()
        .map(PrivateListIndividual::getYearOfBirth)
        .filter(StringUtils::isNotBlank)
        .map(Object::toString)
        .map(s -> s.split(" "))
        .flatMap(Stream::of)
        .distinct();

    return result(mpDobsExtracted, mpYobsExtracted);
  }
}
