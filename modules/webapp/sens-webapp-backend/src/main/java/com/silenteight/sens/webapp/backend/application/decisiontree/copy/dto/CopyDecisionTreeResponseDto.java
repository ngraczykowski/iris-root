package com.silenteight.sens.webapp.backend.application.decisiontree.copy.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CopyDecisionTreeResponseDto {

  @NotNull
  private Long targetId;
}
