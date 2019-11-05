package com.silenteight.sens.webapp.kernel.security.authority;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static com.silenteight.sens.webapp.kernel.security.authority.Authority.*;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public enum Role implements GrantedAuthority {
  ROLE_AUDITOR(AUDIT_GENERATE_REPORT),
  ROLE_INBOX_OPERATOR(INBOX_MANAGE),
  ROLE_USER_MANAGER(USER_MANAGE, USER_VIEW, WORKFLOW_MANAGE),
  ROLE_ANALYST(SOLUTION_VIEW),
  ROLE_BATCH_TYPE_MANAGER(DECISION_TREE_LIST, BATCH_TYPE_MANAGE),
  ROLE_DECISION_TREE_MANAGER(DECISION_TREE_LIST, DECISION_TREE_MANAGE),
  ROLE_DECISION_TREE_VIEWER(DECISION_TREE_LIST),
  ROLE_MAKER(DECISION_TREE_LIST),
  ROLE_APPROVER(DECISION_TREE_LIST);

  private final List<GrantedAuthority> grantedAuthorities;

  Role(GrantedAuthority... authorities) {
    this.grantedAuthorities = asList(authorities);
  }

  @Override
  public String getAuthority() {
    return name();
  }

  public List<GrantedAuthority> getGrantedAuthorities() {
    return unmodifiableList(grantedAuthorities);
  }

  public static List<String> getRoleNames() {
    return stream(values()).map(Enum::name).collect(toList());
  }
}
