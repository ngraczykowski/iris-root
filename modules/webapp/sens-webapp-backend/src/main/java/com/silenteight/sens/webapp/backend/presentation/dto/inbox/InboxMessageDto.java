package com.silenteight.sens.webapp.backend.presentation.dto.inbox;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;

@Builder
@Data
public class InboxMessageDto {

  @NonNull
  private final Long id;
  @NonNull
  private final String type;
  @NonNull
  private final String referenceId;
  @NonNull
  private final String message;
  @NonNull
  private final String state;
  @NonNull
  private final Instant date;
  private final ObjectNode extra;
}
