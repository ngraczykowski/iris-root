/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.domain;

public record MatchFeatureValue(long matchId, long agentConfigFeatureId, String solution,
                                String reason) {
}
