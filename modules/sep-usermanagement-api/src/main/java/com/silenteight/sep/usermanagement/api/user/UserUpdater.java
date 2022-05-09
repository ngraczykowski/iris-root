package com.silenteight.sep.usermanagement.api.user;

import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.user.dto.UpdateUserCommand;

public interface UserUpdater {

  void update(@NonNull UpdateUserCommand command);

  class UserUpdateException extends RuntimeException {

    private static final long serialVersionUID = 9214682747109743854L;

    private UserUpdateException(@NonNull String username, Exception e) {
      super("Could not update user " + username, e);
    }

    public static UserUpdateException of(@NonNull String username, Exception e) {
      return new UserUpdateException(username, e);
    }
  }
}
