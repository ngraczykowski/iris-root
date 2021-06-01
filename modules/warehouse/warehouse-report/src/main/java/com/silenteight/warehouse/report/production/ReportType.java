package com.silenteight.warehouse.report.production;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReportType {

  AI_REASONING("ai_reasoning", "AI Reasoning"),
  ACCURACY("accuracy", "Accuracy");

  private String tenantSuffix;

  private String reportDefinitionTitle;

  public String getTenantName(String env, String flow) {
    return String.join("_", env, flow, tenantSuffix);
  }

  String getTitle() {
    return reportDefinitionTitle;
  }
}
