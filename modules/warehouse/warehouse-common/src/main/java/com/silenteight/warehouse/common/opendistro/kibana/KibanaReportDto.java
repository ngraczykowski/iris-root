package com.silenteight.warehouse.common.opendistro.kibana;

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
