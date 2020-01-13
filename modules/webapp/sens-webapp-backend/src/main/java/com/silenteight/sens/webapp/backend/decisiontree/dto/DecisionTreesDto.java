package com.silenteight.sens.webapp.backend.decisiontree.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class DecisionTreesDto {

  private final int total;
  @NonNull
  private final List<DecisionTreeDto> results;

  public DecisionTreesDto(List<DecisionTreeDto> decisionTrees) {
    total = decisionTrees.size();
    results = decisionTrees;
  }
}
