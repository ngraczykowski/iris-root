package com.silenteight.sens.webapp.backend.changerequest.cancel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
class CancelChangeRequestDto {

  @NotBlank
  private String cancellerComment;
}
