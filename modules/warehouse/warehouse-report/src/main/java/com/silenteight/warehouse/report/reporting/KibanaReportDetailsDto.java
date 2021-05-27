package com.silenteight.warehouse.report.reporting;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
class KibanaReportDetailsDto {

  @NonNull
  private String id;
  @NonNull
  private String title;
}
