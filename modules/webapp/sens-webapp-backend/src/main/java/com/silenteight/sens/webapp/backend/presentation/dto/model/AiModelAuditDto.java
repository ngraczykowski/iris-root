package com.silenteight.sens.webapp.backend.presentation.dto.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Data
@Builder
public class AiModelAuditDto {

  @NonNull
  private final Long aiModelId;
  @NonNull
  private final String aiModelName;
  @NonNull
  private final String auditedOperation;
  @NonNull
  private final Instant auditedAt;
}
