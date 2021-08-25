package com.silenteight.warehouse.indexer.alert;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class AlertSearchCriteria {

  @NonNull
  private String timeFieldName;
  @NonNull
  private String timeRangeFrom;
  @NonNull
  private String timeRangeTo;
  @NonNull
  private List<MultiValueEntry> filter;
  private int alertLimit;
}
