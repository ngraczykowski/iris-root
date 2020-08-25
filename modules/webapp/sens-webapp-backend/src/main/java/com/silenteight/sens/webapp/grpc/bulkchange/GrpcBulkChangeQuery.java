package com.silenteight.sens.webapp.grpc.bulkchange;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.BulkBranchChangeGovernanceGrpc.BulkBranchChangeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.State;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest.BulkBranchChangeIdsFilter;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest.ReasoningBranchFilter;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest.StateFilter;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.protocol.utils.Uuids;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeDto;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIdsForReasoningBranchDto;
import com.silenteight.sens.webapp.backend.bulkchange.closed.ClosedBulkChangeQuery;
import com.silenteight.sens.webapp.backend.bulkchange.pending.PendingBulkChangeQuery;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;
import com.silenteight.sens.webapp.grpc.BranchSolutionMapper;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import io.vavr.control.Try;

import java.util.List;
import java.util.UUID;

import static com.google.rpc.Code.NOT_FOUND;
import static com.silenteight.proto.serp.v1.api.BulkBranchChangeView.State.STATE_APPLIED;
import static com.silenteight.proto.serp.v1.api.BulkBranchChangeView.State.STATE_CREATED;
import static com.silenteight.proto.serp.v1.api.BulkBranchChangeView.State.STATE_REJECTED;
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
import static java.util.List.of;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class GrpcBulkChangeQuery implements ClosedBulkChangeQuery, PendingBulkChangeQuery {

  public static final List<State> PENDING_STATES = of(STATE_CREATED);
  public static final List<State> CLOSED_STATES = of(STATE_APPLIED, STATE_REJECTED);

  @NonNull
  private final BulkBranchChangeGovernanceBlockingStub bulkBranchChangeStub;

  @Override
  public List<BulkChangeDto> listPending() {
    return this.listByState(PENDING_STATES, emptyList());
  }

  @Override
  public List<BulkChangeDto> listClosed(List<UUID> bulkChangeIds) {
    return this.listByState(CLOSED_STATES, bulkChangeIds);
  }

  public List<BulkChangeDto> listByState(List<State> states, List<UUID> bulkChangeIds) {
    ListBulkBranchChangesRequest request =
        ListBulkBranchChangesRequest
            .newBuilder()
            .setStateFilter(filterWith(states))
            .setBulkBranchChangeIdsFilter(filterWithBulkBranchChangeIds(bulkChangeIds))
            .build();

    Try<List<BulkChangeDto>> features =
        of(() -> bulkBranchChangeStub
            .listBulkBranchChanges(request)
            .getChangesList()
            .stream()
            .map(GrpcBulkChangeQuery::toDto)
            .collect(toList()));

    return processResult(features);
  }

  private static BulkBranchChangeIdsFilter filterWithBulkBranchChangeIds(
      List<UUID> bulkBranchChangeIds) {
    return BulkBranchChangeIdsFilter.newBuilder()
        .addAllBulkBranchChangeIds(
            bulkBranchChangeIds.stream()
                .map(Uuids::fromJavaUuid)
                .collect(toList()))
        .build();
  }

  private static StateFilter filterWith(List<State> states) {
    return StateFilter.newBuilder().addAllStates(states).build();
  }

  private static BulkChangeDto toDto(BulkBranchChangeView bulkBranchChangeView) {
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
        reasoningBranchId.getDecisionTreeId(), reasoningBranchId.getFeatureVectorId());
  }

  private static String solutionChangeOf(BulkBranchChangeView bulkBranchChangeView) {
    return bulkBranchChangeView.hasSolutionChange() ?
           BranchSolutionMapper.map(bulkBranchChangeView.getSolutionChange().getSolution()) :
           null;
  }

  private static Boolean statusChangeOf(BulkBranchChangeView bulkBranchChangeView) {
    return bulkBranchChangeView.hasEnablementChange() ?
           bulkBranchChangeView.getEnablementChange().getEnabled() :
           null;
  }

  private static <T> List<T> processResult(Try<List<T>> result) {
    return mapStatusExceptionsToCommunicationException(result)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(codeIs(NOT_FOUND)), () -> success(emptyList())),
                Case($(), () -> failure(exception))))
        .get();
  }

  @Override
  public List<BulkChangeIdsForReasoningBranchDto> getIdsOfPending(
      List<ReasoningBranchIdDto> reasoningBranchIds) {
    return this.getIds(reasoningBranchIds, PENDING_STATES);
  }

  @Override
  public List<BulkChangeIdsForReasoningBranchDto> getIdsOfClosed(
      List<ReasoningBranchIdDto> reasoningBranchIds) {
    return this.getIds(reasoningBranchIds, CLOSED_STATES);
  }

  private List<BulkChangeIdsForReasoningBranchDto> getIds(
      List<ReasoningBranchIdDto> reasoningBranchIds,
      List<State> states) {

    ListBulkBranchChangesRequest request =
        ListBulkBranchChangesRequest
            .newBuilder()
            .setStateFilter(filterWith(states))
            .setReasoningBranchFilter(buildReasoningBranchFilter(reasoningBranchIds))
            .build();

    Try<List<BulkChangeIdsForReasoningBranchDto>> ids =
        of(() -> bulkBranchChangeStub
            .listBulkBranchChanges(request)
            .getChangesList()
            .stream()
            .map(GrpcBulkChangeQuery::toIdDtos)
            .flatMap(List::stream)
            .collect(groupingBy(BulkChangeIdDto::getReasoningBranchId))
            .entrySet()
            .stream()
            .map(entry -> toBulkChangeIdsForReasoningBranchDto(entry.getKey(), entry.getValue()))
            .filter(dto -> reasoningBranchIds.contains(dto.getReasoningBranchId()))
            .collect(toList()));

    return processResult(ids);
  }

  private static ReasoningBranchFilter buildReasoningBranchFilter(
      List<ReasoningBranchIdDto> reasoningBranchIds) {

    return ReasoningBranchFilter
        .newBuilder()
        .addAllReasoningBranchIds(mapToReasoningBranchIds(reasoningBranchIds))
        .build();
  }

  private static Iterable<ReasoningBranchId> mapToReasoningBranchIds(
      List<ReasoningBranchIdDto> reasoningBranchIds) {

    return reasoningBranchIds
        .stream()
        .map(GrpcBulkChangeQuery::mapToReasoningBranchId)
        .collect(toList());
  }

  private static ReasoningBranchId mapToReasoningBranchId(ReasoningBranchIdDto id) {
    return ReasoningBranchId
        .newBuilder()
        .setDecisionTreeId(id.getDecisionTreeId())
        .setFeatureVectorId(id.getFeatureVectorId())
        .build();
  }

  private static List<BulkChangeIdDto> toIdDtos(
      BulkBranchChangeView bulkBranchChangeView) {

    return bulkBranchChangeView
        .getReasoningBranchIdsList()
        .stream()
        .map(id -> toIdDto(bulkBranchChangeView, id))
        .collect(toList());
  }

  private static BulkChangeIdDto toIdDto(
      BulkBranchChangeView bulkBranchChangeView, ReasoningBranchId reasoningBranchId) {

    return new BulkChangeIdDto(
        new ReasoningBranchIdDto(
            reasoningBranchId.getDecisionTreeId(),
            reasoningBranchId.getFeatureVectorId()),
        toJavaUuid(bulkBranchChangeView.getId()));
  }

  private static BulkChangeIdsForReasoningBranchDto toBulkChangeIdsForReasoningBranchDto(
      ReasoningBranchIdDto reasoningBranchId, List<BulkChangeIdDto> ids) {

    return new BulkChangeIdsForReasoningBranchDto(
        reasoningBranchId, mapToBulkChangeIds(ids));
  }

  private static List<UUID> mapToBulkChangeIds(List<BulkChangeIdDto> ids) {
    return ids
        .stream()
        .map(BulkChangeIdDto::getBulkChangeId)
        .collect(toList());
  }

  @Data
  @RequiredArgsConstructor
  private static class BulkChangeIdDto {

    private final ReasoningBranchIdDto reasoningBranchId;
    private final UUID bulkChangeId;
  }
}
