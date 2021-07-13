package com.silenteight.warehouse.report.reporting;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;

@Value(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class ReportInstanceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -415437034149508676L;

  String tenantName;
  String reportDefinitionId;
  Long timestamp;

  public Map<String, Object> toMap() {
    return Map.of("tenantName", tenantName,
                  "reportDefinitionId", reportDefinitionId, "timestamp", timestamp);
  }
}

