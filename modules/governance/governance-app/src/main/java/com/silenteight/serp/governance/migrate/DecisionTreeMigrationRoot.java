package com.silenteight.serp.governance.migrate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "metadata", "modelSchema", "decisionTree" })
class DecisionTreeMigrationRoot {

  @NonNull
  private MigrationMetadata metadata;

  @NonNull
  private ModelSchemaMigration modelSchema;

  @NonNull
  private DecisionTreeMigration decisionTree;

  @JsonIgnore
  List<BranchMigration> getBranches() {
    return decisionTree.getBranches();
  }

  @JsonIgnore
  public List<String> getFeatures() {
    return modelSchema.getFeatures();
  }

  @JsonIgnore
  String getDecisionTreeName() {
    return decisionTree.getName();
  }

  @JsonIgnore
  List<String> getModelSchemaFeatures() {
    return modelSchema.getFeatures();
  }
}
