package com.silenteight.serp.governance.changerequest.approve.event;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class ModelAcceptedEvent {

  @NonNull
  UUID correlationId;
  @NonNull
  String modelName;
  @NonNull
  String promotedBy;
}
