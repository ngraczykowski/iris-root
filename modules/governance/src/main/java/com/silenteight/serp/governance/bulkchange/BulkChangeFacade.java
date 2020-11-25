package com.silenteight.serp.governance.bulkchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesResponse;
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeRequest;
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeResponse;
import com.silenteight.proto.serp.v1.governance.*;

@RequiredArgsConstructor
class BulkChangeFacade implements BulkChangeCommands, BulkChangeQueries {

  private final ApplyBulkBranchChangeHandler applyHandler;
  private final CreateBulkBranchChangeHandler createHandler;
  private final RejectBulkBranchChangeHandler rejectHandler;
  private final ValidateBulkChangeUseCase validateUseCase;
  private final ListBulkBranchChangesUseCase listUseCase;

  @Override
  public BulkBranchChangeAppliedEvent applyBulkBranchChange(
      ApplyBulkBranchChangeCommand command) {
    return applyHandler.apply(command);
  }

  @Override
  public BulkBranchChangeCreatedEvent createBulkBranchChange(
      CreateBulkBranchChangeCommand command) {
    return createHandler.create(command);
  }

  @Override
  public BulkBranchChangeRejectedEvent rejectBulkBranchChange(
      RejectBulkBranchChangeCommand command) {
    return rejectHandler.reject(command);
  }

  @Override
  public ValidateBulkChangeResponse validateBulkBranchChange(
      ValidateBulkChangeRequest request) {
    return validateUseCase.activate(request);
  }

  @Override
  public ListBulkBranchChangesResponse listBulkBranchChanges(
      ListBulkBranchChangesRequest request) {
    return listUseCase.activate(request);
  }
}
