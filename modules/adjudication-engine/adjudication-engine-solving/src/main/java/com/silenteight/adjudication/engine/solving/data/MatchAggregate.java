/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import java.util.Map;

public record MatchAggregate(long matchId,
                             String clientMatchId,
                             Map<String, FeatureAggregate> features,
                             Map<String, CategoryAggregate> categories) {
}
