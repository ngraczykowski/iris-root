package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ApproveChangeRequestCommand {

  private long changeRequestId;

  @NonNull
  private String approverUsername;

  @NonNull
  private String approverComment;
}
