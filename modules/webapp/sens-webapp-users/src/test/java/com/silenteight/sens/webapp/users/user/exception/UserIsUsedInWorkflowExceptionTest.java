package com.silenteight.sens.webapp.users.user.exception;

import com.silenteight.commons.collections.MapBuilder;
import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.user.exception.UserIsUsedInWorkflowException.AssignedTreeView;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

class UserIsUsedInWorkflowExceptionTest {

  private static final String DECISION_TREE_NAME = "DECISION_TREE_NAME";
  private static final String ROLE_MAKER = Role.ROLE_MAKER.getAuthority();
  private static final String ROLE_APPROVER = Role.ROLE_APPROVER.getAuthority();

  @Test
  void emptyList_returnEmptyMap() {
    //given
    UserIsUsedInWorkflowException exception = getException(emptyMap());

    //then
    assertThat(getAssignedTreeViews(exception)).isEmpty();
  }

  @Test
  void oneTree_returnMapWithOneKey() {
    UserIsUsedInWorkflowException exception = getException(MapBuilder.from(
        ROLE_MAKER, singletonList(DECISION_TREE_NAME)));

    assertThat(getAssignedTreeViews(exception))
        .containsExactly(new AssignedTreeView(ROLE_MAKER, singletonList(DECISION_TREE_NAME)));
  }

  @Test
  void multipleTrees_returnMapWithAllValuesGroupedByRole() {
    UserIsUsedInWorkflowException exception = getException(MapBuilder.from(
        ROLE_MAKER, asList(getDecisionTree(1), getDecisionTree(2), getDecisionTree(3)),
        ROLE_APPROVER, asList(getDecisionTree(1), getDecisionTree(3))));

    assertThat(getAssignedTreeViews(exception)).containsExactlyInAnyOrder(
        new AssignedTreeView(ROLE_MAKER,
            asList(getDecisionTree(1), getDecisionTree(2), getDecisionTree(3))),
        new AssignedTreeView(ROLE_APPROVER,
            asList(getDecisionTree(1), getDecisionTree(3))));
  }

  private List<AssignedTreeView> getAssignedTreeViews(UserIsUsedInWorkflowException exception) {
    Map<String, Object> errorMap = exception.getErrorMap();
    assertThat(errorMap).containsKey("assignedTrees");
    return cast(errorMap.get("assignedTrees"));
  }

  private String getDecisionTree(int i) {
    return DECISION_TREE_NAME + i;
  }

  private UserIsUsedInWorkflowException getException(Map<String, List<String>> assignedTrees) {
    Throwable throwable = catchThrowable(
        () -> {
          throw new UserIsUsedInWorkflowException(assignedTrees);
        });
    return (UserIsUsedInWorkflowException) throwable;
  }

  @SuppressWarnings("unchecked")
  private static <T extends List<?>> T cast(Object obj) {
    return (T) obj;
  }
}
