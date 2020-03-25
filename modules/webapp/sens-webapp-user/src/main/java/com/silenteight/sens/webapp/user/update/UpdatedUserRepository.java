package com.silenteight.sens.webapp.user.update;

import lombok.NonNull;

public interface UpdatedUserRepository {

  void save(UpdatedUser updatedUser) throws UserUpdateException;

  class UserUpdateException extends RuntimeException {

    private static final long serialVersionUID = 9214682747109743854L;

    private UserUpdateException(@NonNull String username, Exception e) {
      super("Could not update roles of the user " + username, e);
    }

    public static UserUpdateException of(@NonNull String username, Exception e) {
      return new UserUpdateException(username, e);
    }
  }
}
