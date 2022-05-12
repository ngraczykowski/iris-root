package com.silenteight.warehouse.indexer.query.single;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.warehouse.indexer.alert.AlertColumnName;
import com.silenteight.warehouse.indexer.query.MultiValueEntry;

import java.util.List;

@Data
@Builder
public class AlertSearchCriteria {

  @NonNull
  private AlertColumnName timeFieldName;
  @NonNull
  private String timeRangeFrom;
  @NonNull
  private String timeRangeTo;
  @NonNull
  private List<MultiValueEntry> filter;
  private int alertLimit;
}
