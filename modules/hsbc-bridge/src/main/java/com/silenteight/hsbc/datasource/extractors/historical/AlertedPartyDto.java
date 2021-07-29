package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.Value;

import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyValue;

@Value
public class AlertedPartyDto implements ModelKeyValue {

  String id;
}
