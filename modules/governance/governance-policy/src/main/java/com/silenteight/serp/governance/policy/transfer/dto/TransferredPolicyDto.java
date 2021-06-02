package com.silenteight.serp.governance.policy.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "policyId", "policyName", "description", "steps" })
public class TransferredPolicyDto implements Serializable {

  private static final long serialVersionUID = -9043093192503851984L;

  private UUID policyId;

  @NonNull
  @JsonProperty("policyName")
  private String name;

  private String description;

  @NonNull
  private List<TransferredStepDto> steps;
}
