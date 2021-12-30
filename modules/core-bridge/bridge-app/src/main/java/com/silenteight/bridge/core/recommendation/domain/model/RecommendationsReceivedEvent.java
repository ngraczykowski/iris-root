package com.silenteight.bridge.core.recommendation.domain.model;

import java.util.List;

public record RecommendationsReceivedEvent(String analysisName, List<String> alertNames) {
}
