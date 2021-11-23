package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;
import com.silenteight.hsbc.datasource.util.IsPepTimestampUtil;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map.Entry;

@RequiredArgsConstructor
class WorldCheckEntitiesFurtherInformationExtractor {

  private final List<WorldCheckEntity> worldCheckEntities;

  String extract() {
    return worldCheckEntities.stream()
        .collect(Multimaps.toMultimap(
            entity -> IsPepTimestampUtil.toUnixTimestamp(entity.getLastUpdatedDate()),
            entity -> (StringUtils.isNotBlank(entity.getFurtherInformation())) ?
                      entity.getFurtherInformation() : "",
            HashMultimap::create))
        .entries().stream()
        .max((previousEntry, nextEntry) -> previousEntry.getKey() > nextEntry.getKey() ? 1 : -1)
        .map(Entry::getValue)
        .orElse("");
  }
}
