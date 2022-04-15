package com.silenteight.serp.governance.changerequest.approval.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder
public class ModelApprovalDto {

  @NonNull
  OffsetDateTime approvedAt;
  @NonNull
  String approvedBy;
}
