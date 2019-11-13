package com.silenteight.sens.webapp.backend.application.decisiontree.create.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateDecisionTreeRequestDto {

  @NotNull
  private String name;
  @NotNull
  private Long modelId;
}
