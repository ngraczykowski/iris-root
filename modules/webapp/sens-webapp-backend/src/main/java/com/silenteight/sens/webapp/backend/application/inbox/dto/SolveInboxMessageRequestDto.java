package com.silenteight.sens.webapp.backend.application.inbox.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SolveInboxMessageRequestDto {

  @NotNull
  private Long inboxMessageId;
}
