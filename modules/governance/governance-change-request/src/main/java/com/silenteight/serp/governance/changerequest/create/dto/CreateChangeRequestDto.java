package com.silenteight.serp.governance.changerequest.create.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChangeRequestDto {

  @NonNull
  private UUID id;
  @NotNull
  private String modelName;
  @NotNull
  private String comment;
}
