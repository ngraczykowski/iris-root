package com.silenteight.serp.governance.changerequest.closed.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.annotation.Nullable;

@Value
@Builder
public class ClosedChangeRequestDto {

  @NonNull
  UUID id;
  @NonNull
  String createdBy;
  @NonNull
  OffsetDateTime createdAt;
  @NonNull
  String creatorComment;
  @NonNull
  String decidedBy;
  @NonNull
  OffsetDateTime decidedAt;
  @Nullable
  String deciderComment;
  @NonNull
  String state;
  @NonNull
  String modelName;
}
