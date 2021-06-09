package com.silenteight.serp.governance.policy.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "metadata", "policy" })
public class TransferredPolicyRootDto implements Serializable {

  private static final long serialVersionUID = 8673261502129201342L;

  @NonNull
  private TransferredPolicyMetadataDto metadata;

  @NonNull
  private TransferredPolicyDto policy;

  @JsonIgnore
  public String getCreatedBy() {
    return metadata.getCreatedBy();
  }
}
