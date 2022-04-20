package com.silenteight.qco.domain.model;

public record MatchSolution(String solution, String comment, boolean qcoMarked) {
  public static final boolean QCO_UNMARKED = false;
  public static final boolean QCO_MARKED = true;
}