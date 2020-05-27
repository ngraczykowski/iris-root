package com.silenteight.sens.webapp.backend.changerequest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChangeRequestDto {

  @NotNull
  private UUID bulkChangeId;

  @NotNull
  private OffsetDateTime createdAt;

  @NotNull
  private String comment;
}
