package com.silenteight.sens.webapp.user.sync.analyst.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.lock.LockUserUseCase;
import com.silenteight.sens.webapp.user.lock.LockUserUseCase.LockUserCommand;
import com.silenteight.sens.webapp.user.lock.UnlockUserUseCase;
import com.silenteight.sens.webapp.user.lock.UnlockUserUseCase.UnlockUserCommand;
import com.silenteight.sens.webapp.user.registration.RegisterExternalUserUseCase;
import com.silenteight.sens.webapp.user.registration.RegisterExternalUserUseCase.RegisterExternalUserCommand;
import com.silenteight.sens.webapp.user.sync.analyst.bulk.dto.*;
import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase;
import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase.AddRolesToUserCommand;
import com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCase;
import com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCase.UpdateUserDisplayNameCommand;

import io.vavr.control.Either;

import java.util.List;

import static com.silenteight.sens.webapp.user.sync.analyst.bulk.BulkAnalystService.OperationResult.FAILURE;
import static com.silenteight.sens.webapp.user.sync.analyst.bulk.BulkAnalystService.OperationResult.SUCCESS;
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

  public Result create(BulkCreateAnalystsRequest request) {
    List<OperationResult> operationResults =
        request.asRegisterExternalUserCommands().stream().map(this::create).collect(toList());
    return new Result(operationResults);
  }

  private OperationResult create(RegisterExternalUserCommand command) {
    Either<UserDomainError, RegisterExternalUserUseCase.Success> registration =
        registerExternalUserUseCase.apply(command);

    if (registration.isRight())
      return SUCCESS;
    else
      return FAILURE;
  }

  public Result restore(BulkRestoreAnalystsRequest request) {
    List<OperationResult> operationResults =
        request.asUnlockUserCommands().stream().map(this::restore).collect(toList());
    return new Result(operationResults);
  }

  private OperationResult restore(UnlockUserCommand command) {
    unlockUserUseCase.apply(command);
    return SUCCESS;
  }

  public Result addRole(BulkAddAnalystRoleRequest request) {
    List<OperationResult> operationResults =
        request.asAddRolesToUserCommands().stream().map(this::addRole).collect(toList());
    return new Result(operationResults);
  }

  private OperationResult addRole(AddRolesToUserCommand command) {
    addRolesToUserUseCase.apply(command);
    return SUCCESS;
  }

  public Result updateDisplayName(BulkUpdateDisplayNameRequest request) {
    List<OperationResult> operationResults =
        request
            .asUpdateUserDisplayNameCommands()
            .stream()
            .map(this::updateDisplayName).collect(toList());
    return new Result(operationResults);
  }

  private OperationResult updateDisplayName(UpdateUserDisplayNameCommand command) {
    updateUserDisplayNameUseCase.apply(command);
    return SUCCESS;
  }

  public Result delete(BulkDeleteAnalystsRequest request) {
    List<OperationResult> operationResults =
        request.asLockUserCommands().stream().map(this::delete).collect(toList());
    return new Result(operationResults);
  }

  private OperationResult delete(LockUserCommand command) {
    lockUserUseCase.apply(command);
    return SUCCESS;
  }

  @Value
  public static class Result {

    private static final String MESSAGE_JOINER = " / ";

    private final long success;
    private final long failure;

    public Result(List<OperationResult> operationResults) {
      success = countOperationResults(operationResults, SUCCESS);
      failure = countOperationResults(operationResults, FAILURE);
    }

    private static long countOperationResults(
        List<OperationResult> operationResults, OperationResult expected) {

      return operationResults
          .stream()
          .filter(operationResult -> operationResult == expected)
          .count();
    }

    public String asMessage() {
      return success + MESSAGE_JOINER + (success + failure);
    }
  }

  public enum OperationResult {

    SUCCESS, FAILURE;
  }
}
