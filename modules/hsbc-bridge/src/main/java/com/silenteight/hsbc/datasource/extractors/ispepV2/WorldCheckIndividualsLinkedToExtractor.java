package com.silenteight.hsbc.datasource.extractors.ispepV2;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class WorldCheckIndividualsLinkedToExtractor {

  private final List<WorldCheckIndividual> worldCheckIndividuals;

  public Stream<String> extract() {
    return worldCheckIndividuals.stream().map(WorldCheckIndividual::getLinkedTo);
  }
}
