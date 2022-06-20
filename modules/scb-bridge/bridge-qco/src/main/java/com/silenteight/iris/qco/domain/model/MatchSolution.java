/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain.model;

public record MatchSolution(String solution, String comment, boolean qcoMarked) {
  public static final boolean QCO_UNMARKED = false;
  public static final boolean QCO_MARKED = true;
}
