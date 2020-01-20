package com.silenteight.sens.webapp.kernel.security.authority;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
  DECISION_TREE_LIST,
  DECISION_TREE_MANAGE,
  BATCH_TYPE_MANAGE,
  AUDIT_GENERATE_REPORT,
  INBOX_MANAGE,
  WORKFLOW_MANAGE,
  USER_MANAGE,
  USER_VIEW,
  SOLUTION_VIEW,
  SUPERUSER;

  @Override
  public String getAuthority() {
    return name();
  }
}

