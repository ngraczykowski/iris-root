package com.silenteight.sens.webapp.grpc.bulkchange;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.BulkBranchChangeGovernanceGrpc.BulkBranchChangeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.State;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest.StateFilter;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest.StateFilter.Builder;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeDto;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeQuery;
import com.silenteight.sens.webapp.backend.bulkchange.ReasoningBranchIdDto;
import com.silenteight.sens.webapp.grpc.BranchSolutionMapper;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import io.vavr.control.Try;

import java.util.List;

import static com.google.rpc.Code.NOT_FOUND;
import static com.silenteight.proto.serp.v1.api.BulkBranchChangeView.State.STATE_CREATED;
import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.protocol.utils.Uuids.toJavaUuid;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.codeIs;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.of;
import static io.vavr.control.Try.success;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class GrpcBulkChangeQuery implements BulkChangeQuery {

  @NonNull
  private final BranchSolutionMapper branchSolutionMapper;
  @NonNull
  private final BulkBranchChangeGovernanceBlockingStub bulkBranchChangeStub;

  @Override
  public List<BulkChangeDto> listPending() {
    ListBulkBranchChangesRequest request =
        ListBulkBranchChangesRequest
            .newBuilder()
            .setStateFilter(filterWith(STATE_CREATED))
            .build();

    Try<List<BulkChangeDto>> features =
        of(() -> bulkBranchChangeStub
            .listBulkBranchChanges(request)
            .getChangesList()
            .stream()
            .map(this::toDto)
            .collect(toList()));

    return mapStatusExceptionsToCommunicationException(features)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(codeIs(NOT_FOUND)), () -> success(emptyList())),
                Case($(), () -> failure(exception))))
        .get();
  }

  private static Builder filterWith(State state) {
    return StateFilter.newBuilder().addStates(state);
  }

  private BulkChangeDto toDto(BulkBranchChangeView bulkBranchChangeView) {
    return new BulkChangeDto(
        toJavaUuid(bulkBranchChangeView.getId()),
        reasoningBranchIdDtosOf(bulkBranchChangeView.getReasoningBranchIdsList()),
        solutionChangeOf(bulkBranchChangeView),
        statusChangeOf(bulkBranchChangeView),
        toOffsetDateTime(bulkBranchChangeView.getCreatedAt()));
  }

  private static List<ReasoningBranchIdDto> reasoningBranchIdDtosOf(
      List<ReasoningBranchId> reasoningBranchIdsList) {
    return reasoningBranchIdsList
        .stream()
        .map(GrpcBulkChangeQuery::toDto)
        .collect(toList());
  }

  private static ReasoningBranchIdDto toDto(ReasoningBranchId reasoningBranchId) {
    return new ReasoningBranchIdDto(
        reasoningBranchId.getDecisionTreeId(),
        reasoningBranchId.getFeatureVectorId());
  }

  private String solutionChangeOf(BulkBranchChangeView bulkBranchChangeView) {
    return bulkBranchChangeView.hasSolutionChange() ?
           branchSolutionMapper.map(bulkBranchChangeView.getSolutionChange().getSolution()) :
           null;
  }

  private static Boolean statusChangeOf(BulkBranchChangeView bulkBranchChangeView) {
    return bulkBranchChangeView.hasEnablementChange() ?
           bulkBranchChangeView.getEnablementChange().getEnabled() :
           null;
  }
}
