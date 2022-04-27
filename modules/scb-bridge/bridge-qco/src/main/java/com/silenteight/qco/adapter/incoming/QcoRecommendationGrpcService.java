package com.silenteight.qco.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.qco.api.v1.QcoRecommendation;
import com.silenteight.proto.qco.api.v1.QcoServiceGrpc.QcoServiceImplBase;
import com.silenteight.qco.QcoFacade;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class QcoRecommendationGrpcService extends QcoServiceImplBase {

  private final QcoFacade qcoFacade;

  @Override
  public void submitSolutionForQcoAnalysis(
      QcoRecommendation request, StreamObserver<QcoRecommendation> responseObserver) {
    var qcoRecommendationAlert = QcoRecommendationMapper.toQcoRecommendationAlert(request);
    var updatedQcoRecommendationAlert = qcoFacade.process(qcoRecommendationAlert);

    responseObserver.onNext(
        QcoRecommendationMapper.toQcoRecommendation(updatedQcoRecommendationAlert));
    responseObserver.onCompleted();
  }
}
