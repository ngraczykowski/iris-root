package com.silenteight.sens.webapp.backend.changerequest.cancel;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class CancelChangeRequestCommand {

  private long changeRequestId;

  @NonNull
  private String cancellerUsername;

  @NonNull
  private String cancellerComment;
}
