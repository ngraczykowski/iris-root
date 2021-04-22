package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
class GetHttpRequest {

  @NonNull
  String endpoint;
  @NonNull
  String tenant;
}
