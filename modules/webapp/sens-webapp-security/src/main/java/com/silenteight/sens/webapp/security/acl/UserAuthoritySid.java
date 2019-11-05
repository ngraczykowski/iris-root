package com.silenteight.sens.webapp.security.acl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.security.acls.domain.PrincipalSid;

@Data
@EqualsAndHashCode(callSuper = false)
class UserAuthoritySid extends PrincipalSid {

  private static final long serialVersionUID = 5845163484413763186L;

  private final long userId;

  private final String authority;

  UserAuthoritySid(long userId, String authority) {
    super(userId + "@" + authority);
    this.userId = userId;
    this.authority = authority;
  }

  @Override
  public String toString() {
    return "UserAuthoritySid(principal=" + getPrincipal() + ")";
  }
}
