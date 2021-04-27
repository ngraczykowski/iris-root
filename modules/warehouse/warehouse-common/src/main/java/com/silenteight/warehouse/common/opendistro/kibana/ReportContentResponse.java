package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Data;
import lombok.ToString;

import javax.annotation.Nullable;

@Data
@ToString(onlyExplicitlyIncluded = true)
class ReportContentResponse {

  @Nullable
  // Not included in toString - may contain PII
  private String data;
  @Nullable
  @ToString.Include
  private String filename;
}
