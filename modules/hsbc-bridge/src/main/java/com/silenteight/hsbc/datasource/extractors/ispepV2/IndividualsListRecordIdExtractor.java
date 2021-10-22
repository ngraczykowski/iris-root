package com.silenteight.hsbc.datasource.extractors.ispepV2;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

@RequiredArgsConstructor
class IndividualsListRecordIdExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  public String extract() {
    return worldCheckIndividuals.stream()
        .map(WorldCheckIndividual::getListRecordId)
        .filter(StringUtils::isNotBlank)
        .findFirst()
        .orElse("");
  }
}
