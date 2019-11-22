package com.silenteight.sens.webapp.users.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Builder
@Data
public class UserResponseView {

  private final long total;
  @NonNull
  private final List<UserView> results;
}
