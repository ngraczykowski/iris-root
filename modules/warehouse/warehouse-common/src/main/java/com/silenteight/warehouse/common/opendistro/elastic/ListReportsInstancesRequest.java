package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ListReportsInstancesRequest {

  @NonNull
  String tenant;
}
