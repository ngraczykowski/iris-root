package com.silenteight.bridge.core.registration.domain.command;

import java.util.List;

public record MarkAlertsAsRecommendedCommand(String analysisName, List<String> alertNames) {}
