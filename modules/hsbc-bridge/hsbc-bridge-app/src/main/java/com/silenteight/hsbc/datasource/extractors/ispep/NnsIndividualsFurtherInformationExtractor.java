/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;
import com.silenteight.hsbc.datasource.util.IsPepTimestampUtil;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map.Entry;

@RequiredArgsConstructor
class NnsIndividualsFurtherInformationExtractor {

  private final List<NegativeNewsScreeningIndividuals> nnsIndividuals;

  String extract() {
    return nnsIndividuals.stream()
        .collect(
            Multimaps.toMultimap(
                individual -> IsPepTimestampUtil.toUnixTimestamp(individual.getLastUpdatedDate()),
                individual ->
                    (StringUtils.isNotBlank(individual.getFurtherInformation()))
                        ? individual.getFurtherInformation()
                        : "",
                HashMultimap::create))
        .entries()
        .stream()
        .max((previousEntry, nextEntry) -> previousEntry.getKey() > nextEntry.getKey() ? 1 : -1)
        .map(Entry::getValue)
        .orElse("");
  }
}
