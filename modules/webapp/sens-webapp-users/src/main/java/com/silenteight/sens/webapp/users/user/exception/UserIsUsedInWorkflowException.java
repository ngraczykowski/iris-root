package com.silenteight.sens.webapp.users.user.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.commons.collections.MapBuilder;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Getter
public class UserIsUsedInWorkflowException extends RuntimeException {

  private static final long serialVersionUID = -3627309369809671411L;

  @NonNull
  private final Map<String, List<String>> assignationMap;

  public UserIsUsedInWorkflowException(Map<String, List<String>> assignationMap) {
    super("User has a valid role (" + String.join(", ", assignationMap.keySet())
              + ") used in workflow");
    this.assignationMap = assignationMap;
  }

  public Map<String, Object> getErrorMap() {
    return MapBuilder.from("assignedTrees", getRoleMap());
  }

  private List<AssignedTreeView> getRoleMap() {
    return assignationMap
        .entrySet()
        .stream()
        .map(entrySet -> new AssignedTreeView(entrySet.getKey(), entrySet.getValue()))
        .collect(toList());
  }

  @Value
  static class AssignedTreeView {

    String role;
    List<String> treeList;
  }
}
