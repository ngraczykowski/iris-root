package com.silenteight.sens.webapp.user.sync.analyst.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.user.lock.LockUserUseCase;
import com.silenteight.sens.webapp.user.lock.UnlockUserUseCase;
import com.silenteight.sens.webapp.user.registration.RegisterExternalUserUseCase;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.*;
import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase;
import com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCase;

@RequiredArgsConstructor
public class BulkAnalystService {

  @NonNull
  private final RegisterExternalUserUseCase registerExternalUserUseCase;
  @NonNull
  private final UnlockUserUseCase unlockUserUseCase;
  @NonNull
  private final AddRolesToUserUseCase addRolesToUserUseCase;
  @NonNull
  private final UpdateUserDisplayNameUseCase updateUserDisplayNameUseCase;
  @NonNull
  private final LockUserUseCase lockUserUseCase;

  public void create(BulkCreateAnalystsRequest request) {
    request.asRegisterExternalUserCommands().forEach(registerExternalUserUseCase::apply);
  }

  public void restore(BulkRestoreAnalystsRequest request) {
    request.asUnlockUserCommands().forEach(unlockUserUseCase::apply);
  }

  public void addRole(BulkAddAnalystRoleRequest request) {
    request.asAddRolesToUserCommands().forEach(addRolesToUserUseCase::apply);
  }

  public void updateDisplayName(BulkUpdateDisplayNameRequest request) {
    request.asUpdateUserDisplayNameCommands().forEach(updateUserDisplayNameUseCase::apply);
  }

  public void delete(BulkDeleteAnalystsRequest request) {
    request.asLockUserCommands().forEach(lockUserUseCase::apply);
  }
}
