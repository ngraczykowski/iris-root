package com.silenteight.serp.governance.changerequest.approve;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

import static java.util.UUID.randomUUID;

@Value
@Builder
class ApproveChangeRequestCommand {

  @NonNull
  UUID correlationId = randomUUID();
  @NonNull
  UUID id;
  @NonNull
  String approverUsername;
  @NonNull
  String approverComment;
}
