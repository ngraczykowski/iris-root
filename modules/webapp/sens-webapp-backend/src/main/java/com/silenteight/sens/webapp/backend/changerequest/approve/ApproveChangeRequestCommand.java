package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ApproveChangeRequestCommand {

  @NonNull
  private Long changeRequestId;

  @NonNull
  private String approverUsername;
}
