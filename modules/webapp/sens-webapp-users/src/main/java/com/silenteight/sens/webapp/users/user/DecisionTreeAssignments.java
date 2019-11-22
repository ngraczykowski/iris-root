package com.silenteight.sens.webapp.users.user;

import lombok.Value;

import java.util.List;

public interface DecisionTreeAssignments {

  List<Assignment> retrieveAssignments(long userId);

  @Value
  final class Assignment {

    long decisionTreeId;
    String decisionTreeName;
    String role;
    int level;
  }
}
