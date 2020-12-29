package com.silenteight.serp.governance.policy.importing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "policyId", "policyName", "steps" })
class ImportedPolicy {

  @NonNull
  private UUID policyId;

  @NonNull
  @JsonProperty("policyName")
  private String name;

  @NonNull
  private List<ImportedStep> steps;
}
