package com.silenteight.sens.webapp.backend.changerequest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChangeRequestDto {

  @NotNull
  private UUID bulkChangeId;

  @NonNull
  private OffsetDateTime createdAt;

  @Nullable
  private String comment;
}
