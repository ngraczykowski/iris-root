package com.silenteight.sens.webapp.backend.changerequest.closed;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClosedChangeRequestDto {

  private long id;
  @NonNull
  private UUID bulkChangeId;
  @NonNull
  private String createdBy;
  @NonNull
  private OffsetDateTime createdAt;
  @NonNull
  private String comment;
  @NonNull
  private String decidedBy;
  @NonNull
  private OffsetDateTime decidedAt;
  @NonNull
  private String deciderComment;
  @NonNull
  private String state;
}
