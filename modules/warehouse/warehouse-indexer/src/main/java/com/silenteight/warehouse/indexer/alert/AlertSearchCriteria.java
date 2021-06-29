package com.silenteight.warehouse.indexer.alert;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Map;

@Data
@Builder
public class AlertSearchCriteria {

  @NonNull
  private String timeRangeFrom;
  @NonNull
  private String timeRangeTo;
  @NonNull
  private Map<String, String> filter;
  @NonNull
  private int alertLimit;
}
