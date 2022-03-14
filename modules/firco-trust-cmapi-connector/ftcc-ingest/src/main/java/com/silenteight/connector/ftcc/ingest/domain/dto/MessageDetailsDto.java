package com.silenteight.connector.ftcc.ingest.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class MessageDetailsDto {

  @NonNull
  String batchName;
  @NonNull
  String messageName;
  String payload;
}
