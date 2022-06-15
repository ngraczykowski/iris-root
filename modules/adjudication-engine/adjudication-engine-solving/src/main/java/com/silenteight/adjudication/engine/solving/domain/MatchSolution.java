/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.domain;

import java.util.Map;

public record MatchSolution(
    long analysisId,
    long matchId,
    String clientMatchId,
    String solution,
    String reason,
    Map<String, MatchFeature> features,
    Map<String, MatchCategory> categories
    ) {

}
