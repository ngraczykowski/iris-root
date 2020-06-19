package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ApproveChangeRequestDto {

  @NotBlank
  private String approverComment;
}
