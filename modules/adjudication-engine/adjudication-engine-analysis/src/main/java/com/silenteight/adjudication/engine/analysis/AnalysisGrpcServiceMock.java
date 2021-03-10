package com.silenteight.adjudication.engine.analysis;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceImplBase;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.google.protobuf.util.Timestamps.fromSeconds;
import static io.grpc.Status.INVALID_ARGUMENT;
import static io.grpc.Status.fromCode;
import static java.util.concurrent.ThreadLocalRandom.current;

@GrpcService
class AnalysisGrpcServiceMock extends AnalysisServiceImplBase {

  private static final List<String> MATCHES = List.of("NO_MATCH", "MATCH");
  private static final List<String> SOLUTIONS =
      List.of("SOLUTION A", "SOLUTION B", "SOLUTION C", "SOLUTION D");

  @Override
  public void createAnalysis(CreateAnalysisRequest request,
                             StreamObserver<Analysis> responseObserver) {
    if (!request.hasAnalysis() ||
        request.getAnalysis().getPolicy().isBlank() ||
        request.getAnalysis().getStrategy().isBlank()) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Analysis must be set")
          .asRuntimeException());
      return;
    }

    responseObserver.onNext(Analysis
                    .newBuilder()
                    .setName("analysis/" + UUID.randomUUID())
                    .setPolicy("policy/54317992-a203-4e95-b145-b47d6a3aef0a")
                    .setStrategy("strategy/back_test")
                    .setCreateTime(Timestamp.newBuilder()
                                            .setSeconds(Instant.now().getEpochSecond()))
                    .setState(Analysis.State.NEW)
                    .build());
    responseObserver.onCompleted();
  }

  @Override
  public void addDataset(AddDatasetRequest request,
                         StreamObserver<AnalysisDataset> responseObserver) {
    if (request.getAnalysis().isBlank() || request.getDataset().isBlank()) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Analysis and dataset name must be set")
          .asRuntimeException());
      return;
    }

    responseObserver.onNext(AnalysisDataset
        .newBuilder()
        .setName(request.getAnalysis() + "/" + request.getDataset())
        .setAlertCount(current().nextInt(1, 100))
        .setPendingAlerts(current().nextInt(0, 10))
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void batchAddDatasets(BatchAddDatasetsRequest request,
                               StreamObserver<BatchAddDatasetsResponse> responseObserver) {
    if (request.getAnalysis().isBlank() || request.getDatasetsCount() == 0) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Analysis name must be set")
          .asRuntimeException());
      return;
    }

    var resultBuilder = BatchAddDatasetsResponse.newBuilder();

    for (int i = 0; i < request.getDatasetsCount(); i++) {
      resultBuilder.setAnalysisDatasets(i, AnalysisDataset
          .newBuilder()
          .setName(request.getAnalysis() + "/" + request.getDatasets(i))
          .setAlertCount(current().nextInt(1, 100))
          .setPendingAlerts(current().nextInt(0, 10))
          .build());
    }

    responseObserver.onNext(resultBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void getAnalysis(GetAnalysisRequest request,
                          StreamObserver<Analysis> responseObserver) {
    if (request.getAnalysis().isBlank()) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Analysis name must be set")
          .asRuntimeException());
      return;
    }

    responseObserver.onNext(Analysis
        .newBuilder()
        .setName(request.getAnalysis())
        .setPolicy("policy/54317992-a203-4e95-b145-b47d6a3aef0a")
        .setStrategy("strategy/back_test")
        .setCreateTime(fromSeconds(1614945644))
        .setState(Analysis.State.NEW)
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void streamRecommendations(StreamRecommendationsRequest request,
                                    StreamObserver<Recommendation> responseObserver) {
    if (request.getAnalysis().isBlank()) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Analysis name must be set")
          .asRuntimeException());
      return;
    }

    IntStream.range(0, current().nextInt(1, 10))
        .forEach(i -> responseObserver.onNext(Recommendation
            .newBuilder()
            .setName("analysis/" + request.getAnalysis() +
                      "/recommendations/" + UUID.randomUUID())
            .setCreateTime(fromSeconds(1614945644))
            .build()));
    responseObserver.onCompleted();
  }

  @Override
  public void getRecommendation(GetRecommendationRequest request,
                                StreamObserver<Recommendation> responseObserver) {
    if (request.getRecommendation().isBlank()) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Recommendation name must be set")
          .asRuntimeException());
      return;
    }

    responseObserver.onNext(Recommendation
        .newBuilder()
        .setName(request.getRecommendation())
        .setCreateTime(fromSeconds(1614945644))
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void streamMatchSolutions(StreamMatchSolutionsRequest request,
                                   StreamObserver<MatchSolution> responseObserver) {
    if (request.getAnalysis().isBlank()) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Analysis name must be set")
          .asRuntimeException());
      return;
    }

    IntStream.range(0, current().nextInt(1, 10))
        .forEach(i -> responseObserver.onNext(MatchSolution
            .newBuilder()
            .setName("analysis/" + request.getAnalysis() +
                      "/match_solutions/" + UUID.randomUUID())
            .setMatch(MATCHES.get(current().nextInt(0, MATCHES.size())))
            .setCreateTime(fromSeconds(1614945644))
            .setSolution(SOLUTIONS.get(current().nextInt(0, SOLUTIONS.size())))
            .build()));
    responseObserver.onCompleted();
  }

  @Override
  public void getMatchSolution(GetMatchSolutionRequest request,
                               StreamObserver<MatchSolution> responseObserver) {
    if (request.getMatchSolution().isBlank()) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Match solution name must be set")
          .asRuntimeException());
      return;
    }

    responseObserver.onNext(MatchSolution
        .newBuilder()
        .setName(request.getMatchSolution())
        .setMatch("NO_MATCH")
        .setCreateTime(fromSeconds(1614945644))
        .setSolution("Investigate")
        .build());
    responseObserver.onCompleted();
  }
}
