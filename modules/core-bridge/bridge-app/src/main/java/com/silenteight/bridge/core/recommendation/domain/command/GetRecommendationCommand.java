package com.silenteight.bridge.core.recommendation.domain.command;

import java.util.List;

public record GetRecommendationCommand(String analysisName, List<String> alertNames) {}
