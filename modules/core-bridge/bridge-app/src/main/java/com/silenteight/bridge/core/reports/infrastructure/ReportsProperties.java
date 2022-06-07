package com.silenteight.bridge.core.reports.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;
import javax.validation.constraints.NotEmpty;

@ConstructorBinding
@ConfigurationProperties(prefix = "reports")
public record ReportsProperties(
    boolean streamingEnabled,
    Integer registrationApiToReportsAlertsChunkSize,
    @NotEmpty Map<String, String> customerRecommendationMap
) {}
