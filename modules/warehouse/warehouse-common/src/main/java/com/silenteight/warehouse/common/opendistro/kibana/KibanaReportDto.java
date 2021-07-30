package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Value
@Builder
public class KibanaReportDto {

  @NonNull
  String content;
  @NonNull
  String filename;

  public boolean isContentBlank() {
    return isBlank(content);
  }
}
