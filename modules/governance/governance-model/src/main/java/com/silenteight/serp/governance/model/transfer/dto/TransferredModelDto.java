package com.silenteight.serp.governance.model.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.dto.TransferredPolicyRootDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "metadata", "policy" })
public class TransferredModelDto implements Serializable {

  private static final long serialVersionUID = -4607958773386317798L;

  @NonNull
  private TransferredModelMetadataDto metadata;

  @NonNull
  private TransferredPolicyRootDto policy;

  @JsonIgnore
  public UUID getModelId() {
    return metadata.getModelId();
  }

  @JsonIgnore
  public String getApprovedBy() {
    return metadata.getApprovedBy();
  }

  @JsonIgnore
  public String getPolicyCreatedBy() {
    return policy.getCreatedBy();
  }

  @JsonIgnore
  public String getModelVersion() {
    return metadata.getModelVersion();
  }
}
