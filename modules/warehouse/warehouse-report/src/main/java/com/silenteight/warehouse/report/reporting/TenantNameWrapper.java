package com.silenteight.warehouse.report.reporting;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
class TenantNameWrapper {

  @NonNull
  private String tenantName;
}
