package com.silenteight.adjudication.engine.analysis;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceImplBase;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.fromInstant;
import static io.grpc.Status.INVALID_ARGUMENT;
import static io.grpc.Status.fromCode;
import static java.util.concurrent.ThreadLocalRandom.current;

@GrpcService
class AnalysisGrpcServiceMock extends AnalysisServiceImplBase {

  private static final List<String> SOLUTIONS =
      List.of("FALSE_POSITIVE", "POTENTIAL_TRUE_POSITIVE", "NO_DECISION");
  private static final List<String> ACTIONS =
      List.of("INVESTIGATE", "FALSE_POSITIVE", "POTENTIAL_TRUE_POSITIVE", "HINTED_FALSE_POSITIVE");

  @Override
  public void createAnalysis(
      CreateAnalysisRequest request,
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
        .newBuilder(request.getAnalysis())
        .setName("analysis/" + UUID.randomUUID())
        .setCreateTime(makeTimestampNow())
        .setState(Analysis.State.NEW)
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void addDataset(
      AddDatasetRequest request,
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
  public void batchAddDatasets(
      BatchAddDatasetsRequest request,
      StreamObserver<BatchAddDatasetsResponse> responseObserver) {
    if (request.getAnalysis().isBlank() || request.getDatasetsCount() == 0) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Analysis name and datasets must be set")
          .asRuntimeException());
      return;
    }

    var resultBuilder = BatchAddDatasetsResponse.newBuilder();

    for (var dataset : request.getDatasetsList()) {
      resultBuilder.addAnalysisDatasets(AnalysisDataset
          .newBuilder()
          .setName(request.getAnalysis() + "/" + dataset)
          .setAlertCount(current().nextInt(1, 100))
          .setPendingAlerts(current().nextInt(0, 10))
          .build());
    }

    responseObserver.onNext(resultBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void getAnalysis(
      GetAnalysisRequest request,
      StreamObserver<Analysis> responseObserver) {
    if (request.getAnalysis().isBlank()) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Analysis name must be set")
          .asRuntimeException());
      return;
    }

    var pendingAlerts = current().nextInt(1, 2000);
    var alertCount = current().nextInt(pendingAlerts, pendingAlerts + 10000);

    responseObserver.onNext(Analysis
        .newBuilder()
        .setName(request.getAnalysis())
        .setPolicy("policies/" + UUID.randomUUID().toString())
        .setStrategy("strategies/back_test")
        .addFeatures(Feature.newBuilder()
            .setFeature("features/nationality")
            .setAgentConfig("agents/country/versions/2.0.0/config/1"))
        .addFeatures(Feature.newBuilder()
            .setFeature("features/name")
            .setAgentConfig("agents/name/versions/3.1.0/config/1"))
        .addCategories("DENY")
        .setCreateTime(makePastTimestamp())
        .setState(Analysis.State.NEW)
        .setPendingAlerts(pendingAlerts)
        .setAlertCount(alertCount)
        .putLabels("batch_type", "US_PERD_DENY")
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void streamRecommendations(
      StreamRecommendationsRequest request,
      StreamObserver<Recommendation> responseObserver) {
    if (request.getAnalysis().isBlank()) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Analysis name must be set")
          .asRuntimeException());
      return;
    }

    IntStream.range(0, current().nextInt(1, 10)).forEach(i -> {
      var action = ACTIONS.get(current().nextInt(0, ACTIONS.size()));

      responseObserver.onNext(Recommendation
          .newBuilder()
          .setName(request.getAnalysis() + "/recommendations/" + i)
          .setCreateTime(makePastTimestamp())
          .setAlert("alerts/" + current().nextInt(100, 10000))
          .setRecommendationComment(
              "That is a " + action.toLowerCase().replace('_', ' ') + ", mate!")
          .setRecommendedAction(action)
          .build());
    });
    responseObserver.onCompleted();
  }

  @Override
  public void getRecommendation(
      GetRecommendationRequest request,
      StreamObserver<Recommendation> responseObserver) {
    if (request.getRecommendation().isBlank()) {
      responseObserver.onError(fromCode(INVALID_ARGUMENT.getCode())
          .withDescription("Recommendation name must be set")
          .asRuntimeException());
      return;
    }

    var action = ACTIONS.get(current().nextInt(0, ACTIONS.size()));

    responseObserver.onNext(Recommendation
        .newBuilder()
        .setName(request.getRecommendation())
        .setCreateTime(makePastTimestamp())
        .setAlert("alerts/" + current().nextInt(100, 10000))
        .setRecommendationComment("That is a " + action.toLowerCase().replace('_', ' ') + ", mate!")
        .setRecommendedAction(action)
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void streamMatchSolutions(
      StreamMatchSolutionsRequest request,
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
            .setName(request.getAnalysis() + "/match-solutions/" + i)
            .setMatch("matches/" + current().nextInt(1000, 10000))
            .setCreateTime(makePastTimestamp())
            .setSolution(SOLUTIONS.get(current().nextInt(0, SOLUTIONS.size())))
            .build()));
    responseObserver.onCompleted();
  }

  @Override
  public void getMatchSolution(
      GetMatchSolutionRequest request,
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
        .setMatch("matches/" + current().nextInt(1000, 10000))
        .setCreateTime(makePastTimestamp())
        .setSolution(ACTIONS.get(current().nextInt(0, ACTIONS.size())))
        .build());
    responseObserver.onCompleted();
  }

  private Timestamp makeTimestampNow() {
    return fromInstant(Instant.now());
  }

  private Timestamp makePastTimestamp() {
    // NOTE(ahaczewski): 7*24*60*60 = 604800 seconds = 7 days.
    return fromInstant(Instant.now().minusSeconds(current().nextInt(60, 7 * 24 * 60 * 60)));
  }
}
