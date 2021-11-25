package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyValue;

@Value
@Builder
public class AlertedPartyDto implements ModelKeyValue {

  String id;
}
