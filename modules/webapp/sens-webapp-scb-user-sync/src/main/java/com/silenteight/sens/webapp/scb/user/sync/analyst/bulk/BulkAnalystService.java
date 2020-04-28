package com.silenteight.sens.webapp.scb.user.sync.analyst.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.dto.*;
import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.lock.LockUserUseCase;
import com.silenteight.sens.webapp.user.lock.LockUserUseCase.LockUserCommand;
import com.silenteight.sens.webapp.user.lock.UnlockUserUseCase;
import com.silenteight.sens.webapp.user.lock.UnlockUserUseCase.UnlockUserCommand;
import com.silenteight.sens.webapp.user.registration.RegisterExternalUserUseCase;
import com.silenteight.sens.webapp.user.registration.RegisterExternalUserUseCase.RegisterExternalUserCommand;
import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase;
import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase.AddRolesToUserCommand;
import com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCase;
import com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCase.UpdateUserDisplayNameCommand;

import io.vavr.control.Either;

import java.util.List;

import static com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.SingleResult.failure;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.bulk.SingleResult.success;
import static java.util.stream.Collectors.toList;

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

  public BulkResult create(BulkCreateAnalystsRequest request) {
    List<SingleResult> results =
        request.asRegisterExternalUserCommands().stream().map(this::create).collect(toList());
    return new BulkResult(results);
  }

  private SingleResult create(RegisterExternalUserCommand command) {
    Either<UserDomainError, RegisterExternalUserUseCase.Success> registration =
        registerExternalUserUseCase.apply(command);

    if (registration.isRight())
      return success();
    else
      return failure(registration.getLeft().getReason());

  }

  public BulkResult restore(BulkRestoreAnalystsRequest request) {
    List<SingleResult> results =
        request.asUnlockUserCommands().stream().map(this::restore).collect(toList());
    return new BulkResult(results);
  }

  private SingleResult restore(UnlockUserCommand command) {
    unlockUserUseCase.apply(command);
    return success();
  }

  public BulkResult addRole(BulkAddAnalystRoleRequest request) {
    List<SingleResult> operationResults =
        request.asAddRolesToUserCommands().stream().map(this::addRole).collect(toList());
    return new BulkResult(operationResults);
  }

  private SingleResult addRole(AddRolesToUserCommand command) {
    addRolesToUserUseCase.apply(command);
    return success();
  }

  public BulkResult updateDisplayName(BulkUpdateDisplayNameRequest request) {
    List<SingleResult> operationResults =
        request
            .asUpdateUserDisplayNameCommands()
            .stream()
            .map(this::updateDisplayName).collect(toList());
    return new BulkResult(operationResults);
  }

  private SingleResult updateDisplayName(UpdateUserDisplayNameCommand command) {
    updateUserDisplayNameUseCase.apply(command);
    return success();
  }

  public BulkResult delete(BulkDeleteAnalystsRequest request) {
    List<SingleResult> operationResults =
        request.asLockUserCommands().stream().map(this::delete).collect(toList());
    return new BulkResult(operationResults);
  }

  private SingleResult delete(LockUserCommand command) {
    lockUserUseCase.apply(command);
    return success();
  }
}
