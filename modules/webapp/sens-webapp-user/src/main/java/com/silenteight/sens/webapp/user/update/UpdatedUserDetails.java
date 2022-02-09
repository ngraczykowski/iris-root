package com.silenteight.sens.webapp.user.update;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.silenteight.sep.usermanagement.api.user.dto.UpdateUserCommand;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Set;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class UpdatedUserDetails {

  private String username;
  private String displayName;
  private Set<String> newRoles;
  private Boolean locked;
  private OffsetDateTime updateDate;
  private Collection<String> currentRoles;

  public UpdatedUserDetails(
      UpdateUserCommand updatedUser, Set<String> newRoles, Collection<String> currentRoles) {

    this.username = updatedUser.getUsername();
    this.displayName = updatedUser.getDisplayName();
    this.newRoles = newRoles;
    this.locked = updatedUser.getLocked();
    this.updateDate = updatedUser.getUpdateDate();
    this.currentRoles = currentRoles;
  }
}
