package com.silenteight.payments.bridge.mock.ae;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Profile("mockae")
@GrpcService
@RequiredArgsConstructor
class AlertServiceGrpc extends AlertServiceImplBase {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public void createAlert(
      CreateAlertRequest request,
      StreamObserver<Alert> responseObserver) {

    List<Long> ids = jdbcTemplate.query(
        "SELECT alert_name FROM pb_registered_alert",
        (rs, rowNum) -> Long.parseLong(rs.getString(1).split("/")[1])
    );
    responseObserver.onNext(MockAlertUseCase.createAlert(request.getAlert(), new HashSet<>(ids)));
    responseObserver.onCompleted();
  }

  @Override
  public void batchCreateAlerts(
      BatchCreateAlertsRequest request,
      StreamObserver<BatchCreateAlertsResponse> responseObserver) {
    List<Long> ids = jdbcTemplate.query(
        "SELECT alert_name FROM pb_registered_alert",
        (rs, rowNum) -> Long.parseLong(rs.getString(1).split("/")[1])
    );
    responseObserver.onNext(BatchCreateAlertsResponse
        .newBuilder()
        .addAllAlerts(request
            .getAlertsList()
            .stream()
            .map(alert -> MockAlertUseCase.createAlert(alert, new HashSet<>(ids)))
            .collect(
                Collectors.toList()))
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void batchCreateMatches(
      BatchCreateMatchesRequest request,
      StreamObserver<BatchCreateMatchesResponse> responseObserver) {
    responseObserver.onNext(MockAlertUseCase.batchCreateMatches(request));
    responseObserver.onCompleted();
  }

  @Override
  public void batchCreateAlertMatches(
      BatchCreateAlertMatchesRequest request,
      StreamObserver<BatchCreateAlertMatchesResponse> responseObserver) {
    responseObserver.onNext(MockAlertUseCase.batchCreateAlertMatches(request));
    responseObserver.onCompleted();
  }
}
