package com.silenteight.connector.ftcc.ingest.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
class RequestStore {

  @NonNull
  List<UUID> messageIds;
}
