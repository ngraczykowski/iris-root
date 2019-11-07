package com.silenteight.sens.webapp.user.dto;

import lombok.Data;

import com.silenteight.sens.webapp.domain.user.User;
import com.silenteight.sens.webapp.kernel.security.authority.Role;

import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;

@Data
public class UserView {

  private long id;
  private String userName;
  private String displayName;
  private UserType type;
  private List<Role> roles;
  private Set<GrantedAuthority> authorities;
  private Instant lastLoginAt;
  private Instant createdAt;
  private boolean active;
  private boolean superUser;

  public UserView(User user) {
    id = user.getId();
    userName = user.getUserName();
    displayName = user.getDisplayName();
    type = user.isExternalUser() ? UserType.EXTERNAL : UserType.INTERNAL;
    roles = user.getRoles();
    active = user.isActive();
    superUser = user.isSuperUser();
    lastLoginAt = user.getLastLoginAt();
    createdAt = user.getCreatedAt();
  }

  public String getRoleNames() {
    return getRoles().stream().map(Enum::name).collect(joining(","));
  }
}
