package com.silenteight.serp.governance.model.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "modelId", "approvedBy", "exportedAt" })
public class TransferredModelMetadataDto implements Serializable {

  private static final long serialVersionUID = -6272978266988998927L;

  @NonNull
  private UUID modelId;

  @NonNull
  private String approvedBy;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "UTC")
  private Instant approvedAt;
}
