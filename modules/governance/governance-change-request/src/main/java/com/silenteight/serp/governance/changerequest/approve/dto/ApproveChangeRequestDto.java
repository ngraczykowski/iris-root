package com.silenteight.serp.governance.changerequest.approve.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.silenteight.serp.governance.common.web.rest.RestValidationConstants.FIELD_REGEX;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveChangeRequestDto {

  @NotBlank
  @Pattern(regexp = FIELD_REGEX)
  private String approverComment;
}
