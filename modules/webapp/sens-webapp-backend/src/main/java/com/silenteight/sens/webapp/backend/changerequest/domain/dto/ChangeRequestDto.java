package com.silenteight.sens.webapp.backend.changerequest.domain.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequestDto {

  private long id;
  @NonNull
  private UUID bulkChangeId;
  @NonNull
  private String createdBy;
  @NonNull
  private OffsetDateTime createdAt;
  @NonNull
  private String comment;
}
