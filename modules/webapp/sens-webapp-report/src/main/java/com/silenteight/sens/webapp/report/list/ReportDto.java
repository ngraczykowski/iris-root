package com.silenteight.sens.webapp.report.list;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class ReportDto {

  @NonNull
  String name;
  @NonNull
  String label;
  @NonNull
  FilterType filter;
}
