package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
class GetHttpRequest {

  @NonNull
  String endpoint;
  @NonNull
  String tenant;
}
