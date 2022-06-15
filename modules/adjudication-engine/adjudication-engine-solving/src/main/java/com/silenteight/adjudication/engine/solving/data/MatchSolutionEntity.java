/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

public record MatchSolutionEntity(long analysisId, long matchId, String solution,
                                  String reason, String matchContext) {
}
