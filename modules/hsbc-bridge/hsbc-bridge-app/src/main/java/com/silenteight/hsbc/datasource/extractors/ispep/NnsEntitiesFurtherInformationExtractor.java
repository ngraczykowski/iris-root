/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningEntities;
import com.silenteight.hsbc.datasource.util.IsPepTimestampUtil;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map.Entry;

@RequiredArgsConstructor
public class NnsEntitiesFurtherInformationExtractor {

  private final List<NegativeNewsScreeningEntities> nnsEntities;

  String extract() {
    return nnsEntities.stream()
        .collect(
            Multimaps.toMultimap(
                nnsEntity -> IsPepTimestampUtil.toUnixTimestamp(nnsEntity.getLastUpdateDate()),
                nnsEntity ->
                    (StringUtils.isNotBlank(nnsEntity.getFurtherInformation()))
                        ? nnsEntity.getFurtherInformation()
                        : "",
                HashMultimap::create))
        .entries()
        .stream()
        .max((previousEntry, nextEntry) -> previousEntry.getKey() > nextEntry.getKey() ? 1 : -1)
        .map(Entry::getValue)
        .orElse("");
  }
}
