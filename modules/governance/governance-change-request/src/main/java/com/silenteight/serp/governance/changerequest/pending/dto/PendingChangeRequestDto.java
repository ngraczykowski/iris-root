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
  private UUID id;
  @NonNull
  private String createdBy;
  @NonNull
  private OffsetDateTime createdAt;
  @NonNull
  private String comment;
}
