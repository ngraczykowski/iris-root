/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.feature.date;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class NnsIndividualsDateExtractor implements Extractor {

  private final List<NegativeNewsScreeningIndividuals> nnsIndividuals;

  Stream<String> extract() {
    var mpDobsExtracted = nnsIndividuals
        .stream()
        .map(NegativeNewsScreeningIndividuals::getDobs)
        .filter(StringUtils::isNotBlank)
        .map(Object::toString)
        .map(s -> s.split("\\|"))
        .flatMap(Stream::of)
        .distinct();

    var mpYobsExtracted = nnsIndividuals
        .stream()
        .map(NegativeNewsScreeningIndividuals::getYearOfBirth)
        .filter(StringUtils::isNotBlank)
        .map(Object::toString)
        .map(s -> s.split(" "))
        .flatMap(Stream::of)
        .distinct();

    return result(mpDobsExtracted, mpYobsExtracted);
  }
}
