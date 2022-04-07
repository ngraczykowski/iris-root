package com.silenteight.qco.domain.model;

public record ResolverCommand(QcoRecommendationMatch match,
                              ChangeCondition changeCondition,
                              ResolverAction resolverAction) {
}