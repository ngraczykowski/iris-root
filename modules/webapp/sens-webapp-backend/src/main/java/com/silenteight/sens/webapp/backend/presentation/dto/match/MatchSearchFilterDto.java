package com.silenteight.sens.webapp.backend.presentation.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchSearchFilterDto {

  private long matchGroupId;
  @NotNull
  private MatchGroupRelationship matchGroupRelationship;
}
