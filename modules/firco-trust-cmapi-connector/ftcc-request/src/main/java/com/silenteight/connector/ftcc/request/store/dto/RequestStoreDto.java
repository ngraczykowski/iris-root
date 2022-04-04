package com.silenteight.connector.ftcc.request.store.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class RequestStoreDto {

  @NonNull
  List<UUID> messageIds;
}
