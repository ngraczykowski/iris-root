package com.silenteight.payments.bridge.mock.ae;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceImplBase;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse;
import com.silenteight.adjudication.api.v1.CreateAlertRequest;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.List;

@Profile("mockae")
@GrpcService
@RequiredArgsConstructor
class AlertServiceGrpc extends AlertServiceImplBase {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public void createAlert(
      CreateAlertRequest request,
      StreamObserver<Alert> responseObserver) {

    List<Long> ids = jdbcTemplate.query("select alert_name from pb_registered_alert",
        (rs, rowNum) -> Long.parseLong(rs.getString(1).split("/")[1])
    );
    responseObserver.onNext(MockAlertUseCase.createAlert(request.getAlert(), new HashSet<>(ids)));
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
