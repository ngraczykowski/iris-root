package com.silenteight.sens.webapp.backend.decisiontree.details;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DecisionTreeNotFound extends RuntimeException {

  private static final long serialVersionUID = 9034762290116931206L;

  private final long treeId;

  public static DecisionTreeNotFound of(long treeId) {
    return new DecisionTreeNotFound(treeId);
  }
}
