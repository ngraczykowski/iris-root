package com.silenteight.sens.webapp.report;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class ReportGenerationDetails {

  private String status;
  private String ipAddress;
  private Map<String, String> parameters;
}
