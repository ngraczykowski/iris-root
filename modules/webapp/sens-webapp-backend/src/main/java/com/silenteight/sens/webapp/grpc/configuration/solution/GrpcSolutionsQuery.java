package com.silenteight.sens.webapp.grpc.configuration.solution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.governance.BranchSolutionGrpc.BranchSolutionBlockingStub;
import com.silenteight.sens.webapp.backend.configuration.solution.SolutionsQuery;
import com.silenteight.sens.webapp.grpc.BranchSolutionMapper;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import com.google.protobuf.Empty;
import io.vavr.control.Try;

import java.util.List;

import static com.silenteight.sens.webapp.grpc.GrpcCommunicationException.mapStatusExceptionsToCommunicationException;
import static com.silenteight.sep.base.common.logging.LogMarkers.INTERNAL;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class GrpcSolutionsQuery implements SolutionsQuery {

  private final BranchSolutionBlockingStub solutionStub;

  @Override
  public List<String> list() {
    log.info(INTERNAL, "Get solutions using gRPC BranchSolutions");

    return mapStatusExceptionsToCommunicationException(solutions())
        .recoverWith(
            GrpcCommunicationException.class,
            exception -> Match(exception).of(Case($(), () -> failure(exception))))
        .onSuccess(GrpcSolutionsQuery::logSuccess)
        .onFailure(GrpcSolutionsQuery::logFailure)
        .get();
  }

  private Try<List<String>> solutions() {
    return of(() -> solutionStub
        .listAvailableBranchSolutions(Empty.getDefaultInstance())
        .getBranchSolutionsList()
        .stream()
        .map(BranchSolutionMapper::map)
        .collect(toList()));
  }

  private static void logSuccess(List<String> solutions) {
    log.info(INTERNAL, "Found {}  solutions", solutions.size());
  }

  private static void logFailure(Throwable throwable) {
    log.error(INTERNAL, "Could not get solutions", throwable);
  }
}
