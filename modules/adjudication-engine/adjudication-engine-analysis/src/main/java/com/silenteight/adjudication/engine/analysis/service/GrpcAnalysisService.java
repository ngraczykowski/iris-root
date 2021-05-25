package com.silenteight.adjudication.engine.analysis.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceImplBase;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@GrpcService
@Profile("!mock")
@RequiredArgsConstructor
class GrpcAnalysisService extends AnalysisServiceImplBase {

  private final AnalysisService analysisService;

  @Override
  public void createAnalysis(
      CreateAnalysisRequest request,
      StreamObserver<Analysis> responseObserver) {
    responseObserver.onNext(analysisService.createAnalysis(request));
    responseObserver.onCompleted();
  }

  @Override
  public void addDataset(
      AddDatasetRequest request, StreamObserver<AnalysisDataset> responseObserver) {
    responseObserver.onNext(analysisService.addDataset(request));
    responseObserver.onCompleted();
  }

  @Override
  public void batchAddDatasets(
      BatchAddDatasetsRequest request, StreamObserver<BatchAddDatasetsResponse> responseObserver) {
    responseObserver.onNext(analysisService.batchAddDatasets(request));
    responseObserver.onCompleted();
  }

  @Override
  public void getAnalysis(
      GetAnalysisRequest request, StreamObserver<Analysis> responseObserver) {
    responseObserver.onNext(analysisService.getAnalysis(request));
    responseObserver.onCompleted();
  }

  @Override
  public void streamRecommendations(
      StreamRecommendationsRequest request,
      StreamObserver<Recommendation> responseObserver) {
    respondWithNotFound(responseObserver);
  }

  @Override
  public void getRecommendation(
      GetRecommendationRequest request,
      StreamObserver<Recommendation> responseObserver) {
    respondWithNotFound(responseObserver);
  }

  @Override
  public void streamMatchSolutions(
      StreamMatchSolutionsRequest request,
      StreamObserver<MatchSolution> responseObserver) {
    respondWithNotFound(responseObserver);
  }

  @Override
  public void getMatchSolution(
      GetMatchSolutionRequest request,
      StreamObserver<MatchSolution> responseObserver) {
    respondWithNotFound(responseObserver);
  }

  private static void respondWithNotFound(StreamObserver<?> responseObserver) {
    // XXX(ahaczewski): Mocked to return something more useful than UNIMPLEMENTED.
    responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND));
  }
}
