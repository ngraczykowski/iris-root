package com.silenteight.adjudication.engine.alerts;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceImplBase;

import com.google.common.collect.Lists;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@GrpcService
@Slf4j
class MockedAlertGrpcService extends AlertServiceImplBase {

  @Override
  public void createAlert(
      CreateAlertRequest request, StreamObserver<Alert> responseObserver) {
    log.info("Create Alert API alterId: {}", request.getAlert().getAlertId());
    if (!validateAlertRequest(request.getAlert(), responseObserver)) {
      log.error("Alert validation failed");
      return;
    }
    Timestamp creationTimestamp = Timestamps.fromMillis(System.currentTimeMillis());
    Alert responseAlert = Alert.newBuilder(request.getAlert())
        .setCreateTime(creationTimestamp)
        .setName("alerts/" + UUID.randomUUID().toString())
        .build();
    responseObserver.onNext(responseAlert);
    responseObserver.onCompleted();
    log.info("Response alert has been completed with id:{}", responseAlert.getAlertId());
  }


  @Override
  public void batchCreateAlerts(
      BatchCreateAlertsRequest request,
      StreamObserver<BatchCreateAlertsResponse> responseObserver) {
    log.info("Create Batch Alert API alter count: {}", request.getAlertsCount());
    long failedAlerts =
        request
            .getAlertsList()
            .stream()
            .filter(alert -> !validateAlertRequest(alert, responseObserver))
            .count();
    if (failedAlerts > 0) {
      log.error("Batch Create Alerts validation failed");
      return;
    }
    Timestamp creationTimestamp = Timestamps.fromMillis(System.currentTimeMillis());
    List<Alert> responseAlerts = Lists.newArrayList();
    request
        .getAlertsList()
        .stream()
        .sorted(Comparator.comparingInt(Alert::getPriority).reversed())
        .forEach(alert ->
            responseAlerts.add(Alert.newBuilder(alert)
                .setCreateTime(creationTimestamp)
                .setName("alerts/" + UUID.randomUUID().toString())
                .build())
        );
    BatchCreateAlertsResponse response = BatchCreateAlertsResponse.newBuilder()
        .addAllAlerts(responseAlerts)
        .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    log.info("Batch alerts completed with size:{}", response.getAlertsCount());
  }

  @Override
  public void createMatch(
      CreateMatchRequest request, StreamObserver<Match> responseObserver) {
    log.info("Create Match for alert {}", request.getAlert());
    if (!validateMatchRequest(request.getAlert(), request.getMatch(), responseObserver)) {
      log.error("Create Match validation failed");
      return;
    }
    Timestamp creationTimestamp = Timestamps.fromMillis(System.currentTimeMillis());
    Match responseMatch = Match.newBuilder(request.getMatch())
        .setCreateTime(creationTimestamp)
        .setName(request.getAlert() + "/matches/" + UUID.randomUUID().toString())
        .build();
    responseObserver.onNext(responseMatch);
    responseObserver.onCompleted();
    log.info("Match has been completed created:{}", responseMatch.getMatchId());
  }

  @Override
  public void batchCreateAlertMatches(
      BatchCreateAlertMatchesRequest request,
      StreamObserver<BatchCreateMatchesResponse> responseObserver) {
    log.info("Create batch alert matches count: {}", request.getMatchesCount());
    long matchesFailed = request
        .getMatchesList()
        .stream()
        .filter(match -> !validateMatchRequest(request.getAlert(), match, responseObserver))
        .count();
    if (matchesFailed > 0) {
      log.error("Create Alert Matches validation failed");
      return;
    }
    Timestamp creationTimestamp = Timestamps.fromMillis(System.currentTimeMillis());
    BatchCreateMatchesResponse response =
        createAlertMatchesResponse(request, responseObserver, creationTimestamp);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    log.info("Batch alert matches completed with size:{}", response.getMatchesCount());
  }

  @Override
  public void batchCreateMatches(
      BatchCreateMatchesRequest request,
      StreamObserver<BatchCreateMatchesResponse> responseObserver) {
    log.info(
        "Create batch alert matches, alerts matches count: {}", request.getAlertMatchesCount());
    long failedAlertOrMatch = request
        .getAlertMatchesList()
        .stream()
        .filter(b -> !validateAlertName(b.getAlert(), responseObserver) || b.getMatchesList()
            .stream()
            .filter(m -> !validateMatchRequest(b.getAlert(), m, responseObserver))
            .count() > 0)
        .count();
    if (failedAlertOrMatch > 0) {
      log.error("Batch Create Matches validation failed");
      return;
    }
    Timestamp creationTimestamp = Timestamps.fromMillis(System.currentTimeMillis());
    List<Match> matches = Lists.newArrayList();
    request.getAlertMatchesList().stream().forEach(batchAlertRequest -> {
      BatchCreateMatchesResponse alertMatchesResponse = createAlertMatchesResponse(
          batchAlertRequest,
          responseObserver,
          creationTimestamp);
      matches.addAll(alertMatchesResponse.getMatchesList());
    });
    BatchCreateMatchesResponse response = BatchCreateMatchesResponse.newBuilder()
        .addAllMatches(matches)
        .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    log.info("Create batch alerts matches, finished all matches count:{}", matches.size());
  }

  @Override
  public void getAlert(
      GetAlertRequest request, StreamObserver<Alert> responseObserver) {
    log.info("Get alert {}", request.getAlert());
    if (!validateAlertName(request.getAlert(), responseObserver)) {
      log.error("Get Alert request validation failed");
      return;
    }
    Alert alert = Alert.newBuilder()
        .setName(request.getAlert())
        .setAlertId(UUID.randomUUID().toString())
        .setCreateTime(Timestamps.fromMillis(System.currentTimeMillis()))
        .build();

    responseObserver.onNext(alert);
    responseObserver.onCompleted();
  }

  @Override
  public void listAlertMatches(
      ListAlertMatchesRequest request, StreamObserver<ListAlertMatchesResponse> responseObserver) {
    log.info("Get list o alert matches {}", request.getAlert());
    if (!validateAlertName(request.getAlert(), responseObserver)) {
      log.error("List alert matches request validation failed");
      return;
    }
    List<Match> alertMatches = Lists.newArrayList();
    for (int i = 0; i < 100; i++) {
      Match match = Match.newBuilder()
          .setName(request.getAlert() + "/matches/" + UUID.randomUUID().toString())
          .setMatchId(UUID.randomUUID().toString())
          .setCreateTime(Timestamps.fromMillis(System.currentTimeMillis()))
          .build();
      alertMatches.add(match);
    }
    ListAlertMatchesResponse response = ListAlertMatchesResponse.newBuilder()
        .addAllMatches(alertMatches)
        .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getMatch(
      GetMatchRequest request, StreamObserver<Match> responseObserver) {
    log.info("Get match {}", request.getMatch());
    if (!validateMatchRequest(request.getMatch(), responseObserver)) {
      log.error("Get Match request validation failed");
      return;
    }
    Timestamp timestamp = Timestamps.fromMillis(System.currentTimeMillis());
    Match match = Match.newBuilder()
        .setName(request.getMatch())
        .setMatchId(UUID.randomUUID().toString())
        .setCreateTime(timestamp)
        .build();

    responseObserver.onNext(match);
    responseObserver.onCompleted();
  }

  private BatchCreateMatchesResponse createAlertMatchesResponse(
      BatchCreateAlertMatchesRequest request,
      StreamObserver responseObserver, Timestamp creationTimestamp) {
    List<Match> responseMatches = Lists.newArrayList();
    request.getMatchesList().stream().map(match ->
        responseMatches.add(Match
            .newBuilder(match)
            .setName(request.getAlert() + "/matches/" + UUID.randomUUID().toString())
            .setCreateTime(creationTimestamp)
            .build())
    ).collect(Collectors.toList());
    BatchCreateMatchesResponse response = BatchCreateMatchesResponse.newBuilder()
        .addAllMatches(responseMatches)
        .build();
    return response;
  }


  private boolean validateAlertName(
      String alert, StreamObserver responseObserver) {
    if (ObjectUtils.isEmpty(alert)) {
      log.error("Validation failed alert is is empty");
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("alert must be set")
          .asRuntimeException());
      return false;
    }
    return true;
  }

  private boolean validateAlertRequest(
      Alert requestAlert, StreamObserver responseObserver) {
    if (ObjectUtils.isEmpty(requestAlert.getAlertId())) {
      log.error("Validation failed alert is is empty");
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("alertId must be set")
          .asRuntimeException());
      return false;
    }
    return true;
  }

  private boolean validateMatchRequest(String match, StreamObserver responseObserver) {
    if (ObjectUtils.isEmpty(match)) {
      log.error("Validation failed match id is is empty");
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("matchId must be set")
          .asRuntimeException());
      return false;
    }
    return true;
  }

  private boolean validateMatchRequest(String alert, Match match, StreamObserver responseObserver) {
    if (ObjectUtils.isEmpty(alert)) {
      log.error("Validation failed alert in match is is empty");
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("alert must be set")
          .asRuntimeException());
      return false;
    }
    if (ObjectUtils.isEmpty(match.getMatchId())) {
      log.error("Validation failed match id is is empty");
      responseObserver.onError(Status.fromCode(Status.INVALID_ARGUMENT.getCode())
          .withDescription("matchId must be set")
          .asRuntimeException());
      return false;
    }
    return true;
  }
}
