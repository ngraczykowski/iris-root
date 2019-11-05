package com.silenteight.sens.webapp.security.acl;

import com.silenteight.sens.webapp.kernel.domain.DecisionTreeId;
import com.silenteight.sens.webapp.kernel.domain.WorkflowLevel;

import org.springframework.security.access.prepost.PreAuthorize;

class ProtectedService {

  @PreAuthorize("hasPermission(#decisionTreeId, 'DECISION_TREE_CHANGE')")
  public long getIdentity(DecisionTreeId decisionTreeId) {
    return decisionTreeId.getValue();
  }

  @PreAuthorize("hasPermission(#workflowLevel, 'WORKFLOW_LEVEL_ACCEPT')")
  public String getIdentity(WorkflowLevel workflowLevel) {
    return workflowLevel.getDecisionTreeId() + "@" + workflowLevel.getLevel();
  }
}
