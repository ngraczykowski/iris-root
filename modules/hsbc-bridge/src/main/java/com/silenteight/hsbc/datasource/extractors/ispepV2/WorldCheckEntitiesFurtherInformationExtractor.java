package com.silenteight.hsbc.datasource.extractors.ispepV2;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

@RequiredArgsConstructor
class WorldCheckEntitiesFurtherInformationExtractor {

  private final List<WorldCheckEntity> worldCheckEntities;

  public String extract() {
    return worldCheckEntities.stream()
        .map(WorldCheckEntity::getFurtherInformation)
        .filter(StringUtils::isNotBlank)
        .findFirst()
        .orElse("");
  }
}
