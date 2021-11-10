package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckEntitiesCountryCodesExtractor {

  private final List<WorldCheckEntity> worldCheckEntities;

  Stream<String> extract() {
    return worldCheckEntities.stream()
        .map(WorldCheckEntity::getCountryCodesAll)
        .filter(StringUtils::isNotBlank)
        .flatMap(CountryCodesSplitter::splitCountryCodesBySpace)
        .filter(StringUtils::isNotBlank)
        .map(String::strip);
  }
}
