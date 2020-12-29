package com.silenteight.serp.governance.policy.importing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "metadata", "policy" })
class ImportedPolicyRoot {

  @NonNull
  private ImportedMetadata metadata;

  @NonNull
  private ImportedPolicy policy;
}
