package com.silenteight.serp.governance.policy.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "of")
@Value
class DeletePolicyCommand {

  @NonNull
  UUID id;
  @NonNull
  String deletedBy;
}
