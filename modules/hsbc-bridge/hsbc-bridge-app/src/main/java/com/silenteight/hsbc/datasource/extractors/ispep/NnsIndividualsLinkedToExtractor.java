/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class NnsIndividualsLinkedToExtractor {

  private final List<NegativeNewsScreeningIndividuals> nnsIndividuals;

  Stream<String> extract() {
    return nnsIndividuals.stream()
        .map(NegativeNewsScreeningIndividuals::getLinkedTo)
        .filter(StringUtils::isNotBlank);
  }
}
