package com.silenteight.sens.webapp.grpc.circuitbreaker;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.*;
import com.silenteight.proto.serp.v1.api.DiscrepancyCircuitBreakerGrpc.DiscrepancyCircuitBreakerBlockingStub;
import com.silenteight.sens.webapp.backend.circuitbreaker.DiscrepancyCircuitBreakerQuery;
import com.silenteight.sens.webapp.backend.circuitbreaker.DiscrepancyDto;
import com.silenteight.sens.webapp.backend.circuitbreaker.DiscrepantBranchDto;
import com.silenteight.sens.webapp.backend.circuitbreaker.ReasoningBranchIdDto;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import com.google.protobuf.Empty;
import io.vavr.control.Try;

import java.util.List;

import static com.silenteight.protocol.utils.MoreTimestamps.toInstant;
import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class GrpcDiscrepancyCircuitBreakerQuery implements DiscrepancyCircuitBreakerQuery {

  @NonNull
  private final DiscrepancyCircuitBreakerBlockingStub discrepancyBlockingStub;

  @Override
  public List<DiscrepantBranchDto> listBranchesWithDiscrepancies() {
    Try<List<DiscrepantBranchDto>> discrepantBranches =
        of(() -> discrepancyBlockingStub
            .listDiscrepantBranches(Empty.newBuilder().build())
            .getDiscrepantBranchesList()
            .stream()
            .map(GrpcDiscrepancyCircuitBreakerQuery::toDto)
            .collect(toList()));

    return mapStatusExceptionsToCommunicationException(discrepantBranches)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(), () -> failure(exception))))
        .get();
  }

  @Override
  public List<DiscrepantBranchDto> listBranchesWithArchivedDiscrepancies() {
    Try<List<DiscrepantBranchDto>> discrepantBranches =
        of(() -> discrepancyBlockingStub
            .listBranchesWithArchivedDiscrepancies(Empty.newBuilder().build())
            .getDiscrepantBranchesList()
            .stream()
            .map(GrpcDiscrepancyCircuitBreakerQuery::toDto)
            .collect(toList()));

    return mapStatusExceptionsToCommunicationException(discrepantBranches)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(), () -> failure(exception))))
        .get();
  }

  @Override
  public List<Long> listDiscrepancyIds(ReasoningBranchIdDto branchId) {
    Try<List<Long>> discrepancyIds =
        of(() -> discrepancyBlockingStub
            .listDiscrepancyIds(listDiscrepancyIdsRequestOf(
                branchId.getDecisionTreeId(), branchId.getFeatureVectorId()))
            .getDiscrepancyIds()
            .getDiscrepancyIdsList());

    return mapStatusExceptionsToCommunicationException(discrepancyIds)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(), () -> failure(exception))))
        .get();
  }

  @Override
  public List<Long> listArchivedDiscrepancyIds(ReasoningBranchIdDto branchId) {
    Try<List<Long>> discrepancyIds =
        of(() -> discrepancyBlockingStub
            .listArchivedDiscrepancyIds(listDiscrepancyIdsRequestOf(
                branchId.getDecisionTreeId(), branchId.getFeatureVectorId()))
            .getDiscrepancyIds()
            .getDiscrepancyIdsList());

    return mapStatusExceptionsToCommunicationException(discrepancyIds)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(), () -> failure(exception))))
        .get();
  }

  private ListDiscrepancyIdsRequest listDiscrepancyIdsRequestOf(
      long decisionTreeId, long featureVectorId) {
    return ListDiscrepancyIdsRequest.newBuilder()
        .setDiscrepantBranchId(
            DiscrepantBranchId
                .newBuilder()
                .setDecisionTreeId(decisionTreeId)
                .setFeatureVectorId(featureVectorId)
        ).build();
  }


  @Override
  public List<DiscrepancyDto> listDiscrepanciesByIds(List<Long> discrepancyIds) {
    Try<List<DiscrepancyDto>> discrepancies =
        of(() -> discrepancyBlockingStub
            .listDiscrepancies(listDiscrepanciesRequestOf(discrepancyIds))
            .getDiscrepanciesList()
            .stream()
            .map(GrpcDiscrepancyCircuitBreakerQuery::toDto)
            .collect(toList()));

    return mapStatusExceptionsToCommunicationException(discrepancies)
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(
                Case($(), () -> failure(exception))))
        .get();
  }

  private static ListDiscrepanciesRequest listDiscrepanciesRequestOf(List<Long> discrepancyIds) {
    return ListDiscrepanciesRequest
        .newBuilder()
        .setDiscrepancyIds(DiscrepancyIds.newBuilder().addAllDiscrepancyIds(discrepancyIds))
        .build();
  }

  private static DiscrepantBranchDto toDto(DiscrepantBranch discrepantBranch) {
    DiscrepantBranchId branchId = discrepantBranch.getDiscrepantBranchId();
    return new DiscrepantBranchDto(
        toBranchIdDto(branchId),
        toInstant(discrepantBranch.getDetectedAt()));
  }

  private static DiscrepancyDto toDto(Discrepancy discrepantBranch) {
    return DiscrepancyDto.builder()
        .id(discrepantBranch.getId())
        .alertId(discrepantBranch.getAlertId())
        .aiComment(discrepantBranch.getAiComment().getText())
        .aiCommentDate(toInstant(discrepantBranch.getAiComment().getDate()))
        .analystComment(discrepantBranch.getAnalystComment().getText())
        .analystCommentDate(toInstant(discrepantBranch.getAnalystComment().getDate()))
        .build();
  }

  private static ReasoningBranchIdDto toBranchIdDto(DiscrepantBranchId branchId) {
    return new ReasoningBranchIdDto(branchId.getDecisionTreeId(), branchId.getFeatureVectorId());
  }
}
