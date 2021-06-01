package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class KibanaReportDefinitionDto {

  @NonNull
  String id;
  @NonNull
  String reportName;
  @NonNull
  String description;
}
