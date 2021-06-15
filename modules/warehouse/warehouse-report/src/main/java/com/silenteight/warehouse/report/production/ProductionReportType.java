package com.silenteight.warehouse.report.production;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProductionReportType {

  AI_REASONING("ai_reasoning", "AI Reasoning"),
  ACCURACY("accuracy", "Accuracy");

  private static final String FLOW = "production";

  private String tenantSuffix;

  private String reportDefinitionTitle;

  public String getTenantName(String env) {
    return String.join("_", env, FLOW, tenantSuffix);
  }

  String getTitle() {
    return reportDefinitionTitle;
  }
}
