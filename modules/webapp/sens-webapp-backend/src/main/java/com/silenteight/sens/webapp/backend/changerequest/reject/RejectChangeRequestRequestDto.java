package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
class RejectChangeRequestRequestDto {

  @NotBlank
  private String rejectorComment;
}
