package com.silenteight.serp.governance.changerequest.cancel;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CancelChangeRequestCommand {

  @NonNull
  UUID id;
  @NonNull
  String cancellerUsername;
}
