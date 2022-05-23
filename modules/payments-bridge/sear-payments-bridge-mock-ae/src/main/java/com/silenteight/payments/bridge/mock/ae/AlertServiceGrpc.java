package com.silenteight.payments.bridge.mock.ae;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceImplBase;
import com.silenteight.payments.bridge.common.resource.ResourceName;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Profile("mockae")
@GrpcService
@RequiredArgsConstructor
@Slf4j
class AlertServiceGrpc extends AlertServiceImplBase {

  private final AtomicLong aeDatabaseSeq = new AtomicLong(1000);
  private final JdbcTemplate jdbcTemplate;

  @Override
  public void createAlert(
      CreateAlertRequest request, StreamObserver<Alert> responseObserver) {
    responseObserver.onNext(
        MockAlertUseCase.getOrCreate(request.getAlert(), this::fetchDatabaseId));
    responseObserver.onCompleted();
  }

  @Override
  public void batchCreateAlerts(
      BatchCreateAlertsRequest request,
      StreamObserver<BatchCreateAlertsResponse> responseObserver) {

    responseObserver.onNext(BatchCreateAlertsResponse
        .newBuilder()
        .addAllAlerts(request
            .getAlertsList()
            .stream()
            .map(alert -> MockAlertUseCase.getOrCreate(alert, this::fetchDatabaseId))
            .collect(Collectors.toList())).build());
    responseObserver.onCompleted();
  }

  Long fetchDatabaseId(String alertMessageId) {
    try {
      long databaseID =
          jdbcTemplate.queryForObject(
              "SELECT * FROM pb_registered_alert WHERE alert_message_id = ?",
              (rs, rowNum) -> ResourceName.create(rs.getString("alert_name")).getLong("alerts"),
              UUID.fromString(alertMessageId));
      log.info("DatabaseId:{} for alertMessageId:{}", databaseID, alertMessageId);
      return databaseID;
    } catch (EmptyResultDataAccessException e) {
      log.debug("No entry in database create mocked value for alertMessageId: {}", alertMessageId);
    }
    return aeDatabaseSeq.getAndIncrement();
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

  @Override
  public void batchAddLabels(
      BatchAddLabelsRequest request, StreamObserver<BatchAddLabelsResponse> responseObserver) {
    responseObserver.onNext(MockAlertUseCase.batchAddAlertsResponse(request));
    responseObserver.onCompleted();
  }
}
