package com.silenteight.sens.webapp.report;

import com.silenteight.sens.webapp.report.list.FilterType;

public interface ConfigurableReport {

  String getName();

  String getType();

  String getLabel();

  FilterType getFilterType();

  boolean isEnabled();
}
