package com.silenteight.serp.governance.migrate.dto;

import lombok.Value;

@Value
public class ImportedDecisionTree {

  private final long id;
  private final String name;

  public ImportedDecisionTree(long id, String name) {
    this.id = id;
    this.name = name;
  }
}
