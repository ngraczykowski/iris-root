package com.silenteight.sens.webapp.backend.presentation.dto.inbox;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Builder
@Data
public class InboxResposneDto {

  private final long total;
  @NonNull
  private final List<InboxMessageDto> results;
}
