package com.silenteight.sens.webapp.backend.application.decisiontree.patch.dto;

import lombok.Data;

import javax.annotation.Nullable;

@Data
public class PatchDecisionTreeRequestDto {

  @Nullable
  private String name;
}
