package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyValue;

@Builder
@Value
public class WatchlistPartyDto implements ModelKeyValue {

  String id;
  String type;
}
