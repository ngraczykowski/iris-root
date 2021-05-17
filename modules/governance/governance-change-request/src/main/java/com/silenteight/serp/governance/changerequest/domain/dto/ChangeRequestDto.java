package com.silenteight.serp.governance.changerequest.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestState;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.annotation.Nullable;

@Value
@Builder
public class ChangeRequestDto {

  @NonNull
  UUID id;
  @NonNull
  ChangeRequestState state;
  @NonNull
  String createdBy;
  @NonNull
  OffsetDateTime createdAt;
  @NonNull
  String creatorComment;
  @Nullable
  String decidedBy;
  @Nullable
  OffsetDateTime decidedAt;
  @Nullable
  String deciderComment;
  @NonNull
  String modelName;
}
