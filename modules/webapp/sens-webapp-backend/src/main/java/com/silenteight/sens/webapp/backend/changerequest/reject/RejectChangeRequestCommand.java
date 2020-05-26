package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class RejectChangeRequestCommand {

  private long changeRequestId;

  @NonNull
  private String rejectorUsername;
}
