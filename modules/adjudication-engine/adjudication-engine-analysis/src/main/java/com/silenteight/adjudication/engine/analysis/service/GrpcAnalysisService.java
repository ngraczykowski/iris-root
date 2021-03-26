package com.silenteight.adjudication.engine.analysis.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceImplBase;

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
}
