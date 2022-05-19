package com.silenteight.bridge.core.reports.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "reports")
public record ReportsProperties(
    boolean streamingEnabled,
    Integer registrationApiToReportsAlertsChunkSize) {}
