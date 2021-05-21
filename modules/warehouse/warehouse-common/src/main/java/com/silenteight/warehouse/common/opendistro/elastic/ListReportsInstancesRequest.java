package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class ListReportsInstancesRequest {

  @NonNull
  String tenant;
}
