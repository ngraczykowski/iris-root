package com.silenteight.sens.webapp.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.silenteight.sens.webapp.DecisionTreeAssignments.Assignment;
import com.silenteight.sens.webapp.domain.user.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetailedView extends UserView {

  private final List<AssignmentView> assignmentViews;

  public UserDetailedView(User user, List<Assignment> assignments) {
    super(user);
    assignmentViews = assignments.stream().map(AssignmentView::new).collect(toList());
  }

  @Data
  class AssignmentView {

    private final long decisionTreeId;
    private final String decisionTreeName;
    private final String role;
    private final long level;

    AssignmentView(Assignment assignment) {
      decisionTreeId = assignment.getDecisionTreeId();
      decisionTreeName = assignment.getDecisionTreeName();
      role = assignment.getRole();
      level = assignment.getLevel();
    }
  }
}
