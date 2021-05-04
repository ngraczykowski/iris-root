package com.silenteight.serp.governance.changerequest.pending.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class PendingChangeRequestDto {

  @NonNull
  UUID id;
  @NonNull
  String createdBy;
  @NonNull
  OffsetDateTime createdAt;
  @NonNull
  String modelName;
  @NonNull
  String comment;
}
