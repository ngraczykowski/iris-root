package com.silenteight.sep.usermanagement.api.user.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.sep.usermanagement.api.role.UserRoles;

import java.time.OffsetDateTime;
import javax.annotation.Nullable;

@Value
@Builder
@EqualsAndHashCode
public class UpdateUserCommand {

  @NonNull
  String username;
  @Nullable
  String displayName;
  @Nullable
  UserRoles roles;
  @Nullable
  Boolean locked;
  @NonNull
  OffsetDateTime updateDate;
}
