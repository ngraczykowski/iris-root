package com.silenteight.serp.governance.changerequest.approve.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveChangeRequestDto {

  @NotBlank
  private String approverComment;
}
