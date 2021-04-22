package com.silenteight.serp.governance.changerequest.approve;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
class ApproveChangeRequestCommand {

  @NonNull
  UUID id;
  @NonNull
  String approverUsername;
  @NonNull
  String approverComment;
}
