package com.silenteight.serp.governance.policy.importing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "count", "features" })
class ImportedFeatureLogic {

  @NonNull
  private Integer count;

  @NonNull
  private Map<String, List<String>> features;
}
