package com.silenteight.serp.governance.changerequest.reject;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
class RejectChangeRequestCommand {

  @NonNull
  UUID id;
  @NonNull
  String rejectorUsername;
  @NonNull
  String rejectorComment;
}
