package com.silenteight.warehouse.report.simulation;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class TenantDto {

  @NonNull
  private String tenantName;
}
