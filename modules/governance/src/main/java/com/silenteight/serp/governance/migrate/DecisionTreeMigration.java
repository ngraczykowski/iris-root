package com.silenteight.serp.governance.migrate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "decisionTreeId", "decisionTreeName", "reasoningBranches" })
class DecisionTreeMigration {

  @NonNull
  @JsonProperty("decisionTreeId")
  private Long id;

  @NonNull
  @JsonProperty("decisionTreeName")
  private String name;

  @NonNull
  private List<BranchMigration> branches;
}
