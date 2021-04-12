package com.silenteight.hsbc.bridge.alert.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Collection;
import java.util.List;

@Builder
@Value
public class BatchCreateAlertMatchesRequestDto {

  @NonNull String alert;
  @NonNull Collection<String> matchIds;
}
