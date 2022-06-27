package com.silenteight.sens.webapp.report;

import lombok.NonNull;

import com.silenteight.sens.webapp.report.list.FilterType;

public abstract class AbstractConfigurableReport implements ConfigurableReport {

  @NonNull
  protected final ReportProperties reportProperties;

  protected AbstractConfigurableReport(@NonNull ReportProperties reportProperties) {
    this.reportProperties = reportProperties;
  }

  public String getName() {
    return reportProperties.getName();
  }

  public String getType() {
    return reportProperties.getType();
  }

  public String getLabel() {
    return reportProperties.getLabel();
  }

  public FilterType getFilterType() {
    return reportProperties.getFilterType();
  }

  public boolean isEnabled() {
    return reportProperties.isEnabled();
  }
}
