package com.silenteight.serp.governance.policy.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "exportedAt" })
public class TransferredPolicyMetadataDto implements Serializable {

  private static final long serialVersionUID = -3686897739373445722L;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
  private Instant createdAt;

  private String createdBy;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
  private Instant updatedAt;

  private String updatedBy;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
  private Instant exportedAt;
}
