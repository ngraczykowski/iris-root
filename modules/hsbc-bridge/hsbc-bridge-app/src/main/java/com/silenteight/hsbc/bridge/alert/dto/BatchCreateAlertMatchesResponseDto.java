package com.silenteight.hsbc.bridge.alert.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class BatchCreateAlertMatchesResponseDto {

  List<AlertMatchDto> alertMatches;
}
