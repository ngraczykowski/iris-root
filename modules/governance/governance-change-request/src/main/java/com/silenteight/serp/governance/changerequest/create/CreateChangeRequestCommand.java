package com.silenteight.serp.governance.changerequest.create;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
class CreateChangeRequestCommand {

  @NonNull
  UUID id;
  @NonNull
  String modelName;
  @NonNull
  String createdBy;
  @NonNull
  String creatorComment;
}
