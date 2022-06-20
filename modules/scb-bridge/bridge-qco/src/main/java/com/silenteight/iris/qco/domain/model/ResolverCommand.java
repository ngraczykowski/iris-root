/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain.model;

public record ResolverCommand(QcoRecommendationMatch match,
                              ChangeCondition changeCondition,
                              ResolverAction resolverAction) {
}
