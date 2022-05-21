package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto.ModelKeyValue;

@Value
@Builder
public class AlertedPartyDto implements ModelKeyValue {

  String id;
}
