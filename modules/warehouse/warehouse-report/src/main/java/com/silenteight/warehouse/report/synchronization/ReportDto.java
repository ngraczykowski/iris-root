package com.silenteight.warehouse.report.synchronization;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ReportDto {

  @NonNull
  UUID reportId;
  @NonNull
  String tenant;
  @NonNull
  String kibanaReportInstanceId;
  @NonNull
  String filename;
}
