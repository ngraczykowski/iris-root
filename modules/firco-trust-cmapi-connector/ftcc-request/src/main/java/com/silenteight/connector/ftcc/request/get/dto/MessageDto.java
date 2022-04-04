package com.silenteight.connector.ftcc.request.get.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class MessageDto {

  @NonNull
  String batchName;
  @NonNull
  String messageName;
  String payload;
}
