package com.silenteight.warehouse.common.opendistro.kibana.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class KibanaReportDto {

  @NonNull
  String content;
  @NonNull
  String filename;
}
