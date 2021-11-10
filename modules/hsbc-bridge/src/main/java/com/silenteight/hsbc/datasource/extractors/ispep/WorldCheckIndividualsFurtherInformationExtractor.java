package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

@RequiredArgsConstructor
class WorldCheckIndividualsFurtherInformationExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  String extract() {
    return worldCheckIndividuals.stream()
        .map(WorldCheckIndividual::getFurtherInformation)
        .filter(StringUtils::isNotBlank)
        .findFirst()
        .orElse("");
  }
}
