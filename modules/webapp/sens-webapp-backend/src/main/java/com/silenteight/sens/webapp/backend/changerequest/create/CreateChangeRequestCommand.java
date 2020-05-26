package com.silenteight.sens.webapp.backend.changerequest.create;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.annotation.Nullable;

@Value
@Builder
public class CreateChangeRequestCommand {

  @NonNull
  private UUID bulkChangeId;

  @NonNull
  private String makerUsername;

  @Nullable
  private String makerComment;

  @NonNull
  private OffsetDateTime createdAt;
}
