package com.silenteight.sep.usermanagement.api.user;

import lombok.NonNull;

public interface UserRemover {

  void remove(@NonNull String username);
}
