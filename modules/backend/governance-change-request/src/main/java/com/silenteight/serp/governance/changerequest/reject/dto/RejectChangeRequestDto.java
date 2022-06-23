package com.silenteight.serp.governance.changerequest.reject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectChangeRequestDto {

  @NotBlank
  private String rejectorComment;
}
