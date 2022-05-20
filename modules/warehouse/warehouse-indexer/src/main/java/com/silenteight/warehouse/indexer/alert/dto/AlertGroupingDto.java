package com.silenteight.warehouse.indexer.alert.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class AlertGroupingDto {

  @NonNull
  Integer count;
  // Contains pair (name, value) for grouped property. E.g. {"country", "PL"}
  @NonNull
  Map<String, String> groupedNameValueFields;
}
