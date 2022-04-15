package com.silenteight.serp.governance.changerequest.create.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.MAX_MODEL_NAME_LENGTH;
import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.MIN_MODEL_NAME_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChangeRequestDto {

  @NonNull
  private UUID id;
  @Size(min = MIN_MODEL_NAME_LENGTH, max = MAX_MODEL_NAME_LENGTH)
  @NotNull
  private String modelName;
  @NotNull
  private String comment;
}
