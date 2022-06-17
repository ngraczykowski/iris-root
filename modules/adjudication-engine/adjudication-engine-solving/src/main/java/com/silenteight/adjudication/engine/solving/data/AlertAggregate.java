/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import lombok.Builder;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Builder
public record AlertAggregate(long analysisId,
                             long alertId,
                             int priority,
                             Map<Long, MatchAggregate> matches,
                             String policy,
                             String strategy,
                             Map<String, Set<String>> agentFeatures,
                             Map<String, String> labels) implements Serializable {

}
