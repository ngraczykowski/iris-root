package com.silenteight.serp.governance.model.create;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreateModelCommand {

  @NonNull
  UUID id;
  @NonNull
  String policy;
  @NonNull
  String createdBy;
}
