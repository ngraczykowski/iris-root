package com.silenteight.payments.bridge.mock.ae;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

import static java.util.stream.Collectors.toList;

@Profile("mockae")
@GrpcService
@RequiredArgsConstructor
class AlertServiceGrpc extends AlertServiceImplBase {

  private static int alertId = 1;
  private static int matchId = 1;

  @Override
  public void createAlert(
      CreateAlertRequest request,
      StreamObserver<Alert> responseObserver) {
    var alert = request.getAlert();
    responseObserver.onNext(
        Alert.newBuilder().setAlertId(alert.getAlertId()).setName("alerts/" + alertId).build());
    alertId++;
    responseObserver.onCompleted();
  }

  @Override
  public void batchCreateAlertMatches(
      BatchCreateAlertMatchesRequest request,
      StreamObserver<BatchCreateAlertMatchesResponse> responseObserver) {
    responseObserver.onNext(BatchCreateAlertMatchesResponse
        .newBuilder()
        .addAllMatches(request
            .getMatchesList()
            .stream()
            .map(m -> Match
                .newBuilder()
                .setMatchId(m.getMatchId())
                .setName("alerts/" + alertId + "/matches/" + matchId)
                .build())
            .collect(
                toList()))
        .build());
    matchId++;
    responseObserver.onCompleted();
  }
}
