package com.silenteight.sens.webapp.backend.application.decisiontree.copy.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CopyDecisionTreeRequestDto {

  @NotNull
  private Long sourceId;
  @NotBlank
  private String targetName;
}
