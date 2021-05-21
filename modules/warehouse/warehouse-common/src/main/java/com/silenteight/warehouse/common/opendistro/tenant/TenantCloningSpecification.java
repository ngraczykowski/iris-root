package com.silenteight.warehouse.common.opendistro.tenant;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TenantCloningSpecification {

  @NonNull
  String sourceTenant;
  @NonNull
  String targetTenant;
  @NonNull
  String tenantDescription;
  @NonNull
  String elasticIndexName;
}
