package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;
import com.silenteight.hsbc.datasource.util.IsPepTimestampUtil;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map.Entry;

@RequiredArgsConstructor
class WorldCheckIndividualsFurtherInformationExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  String extract() {
    return worldCheckIndividuals.stream()
        .collect(Multimaps.toMultimap(
            individual -> IsPepTimestampUtil.toUnixTimestamp(individual.getLastUpdatedDate()),
            individual -> (StringUtils.isNotBlank(individual.getFurtherInformation())) ?
                          individual.getFurtherInformation() : "",
            HashMultimap::create))
        .entries().stream()
        .max((previousEntry, nextEntry) -> previousEntry.getKey() > nextEntry.getKey() ? 1 : -1)
        .map(Entry::getValue)
        .orElse("");
  }
}
