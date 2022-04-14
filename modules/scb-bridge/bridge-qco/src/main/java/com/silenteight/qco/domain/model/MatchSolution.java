package com.silenteight.qco.domain.model;

public record MatchSolution(String solution, String comment, boolean changed) {
  public static final boolean UNCHANGED = false;
  public static final boolean OVERRIDDEN = true;
}