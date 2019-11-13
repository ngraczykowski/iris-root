package com.silenteight.sens.webapp.backend.presentation.dto.inbox;

import lombok.Data;

import javax.annotation.Nullable;

@Data
public class InboxMessageSearchFilterDto {

  @Nullable
  private String state;

}
