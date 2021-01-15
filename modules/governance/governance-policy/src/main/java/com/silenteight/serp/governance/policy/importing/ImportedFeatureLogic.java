package com.silenteight.serp.governance.policy.importing;

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
@JsonPropertyOrder({ "toFulfill", "matchConditions" })
class ImportedFeatureLogic {

  @NonNull
  @JsonProperty("toFulfill")
  private Integer toFulfill;

  @NonNull
  @JsonProperty("matchConditions")
  private List<MatchCondition> matchConditions;
}
