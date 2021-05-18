package com.silenteight.warehouse.report.reporting;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

@Data
@Builder
public class KibanaReportsList {

  @NonNull
  private Set<KibanaReportDetailsDto> reports;
}
