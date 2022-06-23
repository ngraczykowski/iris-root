package com.silenteight.serp.governance.policy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "toFulfill", "matchConditions" })
public class TransferredFeatureLogicDto implements Serializable {

  private static final long serialVersionUID = 8055332760411371655L;

  @NonNull
  @JsonProperty("toFulfill")
  private Integer toFulfill;

  @NonNull
  @JsonProperty("matchConditions")
  private List<TransferredMatchConditionDto> matchConditions;
}
