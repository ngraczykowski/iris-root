package com.silenteight.warehouse.common.opendistro.tenant;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Value
@Builder
public class TenantCloningResult {

  @NonNull
  @Default
  Map<String, String> reportMapping = Map.of();

  public Set<String> getReportDefinitionIds() {
    return new HashSet<>(reportMapping.values());
  }
}
