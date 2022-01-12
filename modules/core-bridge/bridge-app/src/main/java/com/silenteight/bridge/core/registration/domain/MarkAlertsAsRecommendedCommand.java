package com.silenteight.bridge.core.registration.domain;

import java.util.List;

public record MarkAlertsAsRecommendedCommand(String analysisName, List<String> alertNames) {}
