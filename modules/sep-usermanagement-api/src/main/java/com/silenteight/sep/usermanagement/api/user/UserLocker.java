package com.silenteight.sep.usermanagement.api.user;

import lombok.NonNull;

public interface UserLocker {

  void unlock(@NonNull String username);
}
