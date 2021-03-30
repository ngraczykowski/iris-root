package com.silenteight.hsbc.bridge.adjudication;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.CreateDatasetRequest.FilterCase;

import com.google.common.collect.Lists;
import com.google.protobuf.Timestamp;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.concurrent.ThreadLocalRandom.current;

@Slf4j
class AdjudicationApiMock implements AdjudicationApi {


  public com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse batchCreateAlerts(
      com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest request) {
    log.info("Create Batch Alert API alter count: {}", request.getAlertsCount());
    long failedAlerts =
        request
            .getAlertsList()
            .stream()
            .filter(alert -> !validateAlertRequest(alert))
            .count();
    if (failedAlerts > 0) {
      log.error("Batch Create Alerts validation failed");
      throw new RuntimeException();
    }

    Timestamp creationTimestamp = now();
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

    log.info("Batch alerts completed with size:{}", response.getAlertsCount());

    return response;
  }

  public com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse batchCreateAlertMatches(
      com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest request) {

    log.info("Create batch alert matches count: {}", request.getMatchesCount());
    long matchesFailed = request
        .getMatchesList()
        .stream()
        .filter(match -> !validateMatchRequest(request.getAlert(), match))
        .count();
    if (matchesFailed > 0) {
      log.error("Create Alert Matches validation failed");
      throw new RuntimeException();
    }
    BatchCreateAlertMatchesResponse response =
        createAlertMatchesResponse(request, now());

    log.info("Batch alert matches completed with size:{}", response.getMatchesCount());

    return response;
  }

  @Override
  public Dataset createDataset(CreateDatasetRequest request) {
    if (request.getFilterCase() == FilterCase.FILTER_NOT_SET) {
      throw new RuntimeException("filter must be set");
    }

    int alertsCount;

    if (request.hasNamedAlerts())
      alertsCount = request.getNamedAlerts().getAlertsCount();
    else
      alertsCount = 123;

    return Dataset
        .newBuilder()
        .setName("datasets/" + UUID.randomUUID())
        .setAlertCount(alertsCount)
        .setCreateTime(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()))
        .build();
  }

  private boolean validateAlertRequest(
      Alert requestAlert) {
    if (ObjectUtils.isEmpty(requestAlert.getAlertId())) {
      log.error(
          "Validation failed alert is is empty",
          new RuntimeException());

      return false;
    }
    return true;
  }

  private boolean validateMatchRequest(String alert, Match match) {
    if (ObjectUtils.isEmpty(alert)) {
      log.error("Validation failed alert in match is is empty", new RuntimeException());
      return false;
    }
    if (ObjectUtils.isEmpty(match.getMatchId())) {
      log.error("Validation failed match id is is empty", new RuntimeException());
      return false;
    }
    return true;
  }

  private BatchCreateAlertMatchesResponse createAlertMatchesResponse(
      BatchCreateAlertMatchesRequest request, Timestamp creationTimestamp) {

    List<Match> responseMatches = Lists.newArrayList();
    request.getMatchesList().stream().map(match ->
        responseMatches.add(Match
            .newBuilder(match)
            .setName(request.getAlert() + "/matches/" + UUID.randomUUID().toString())
            .setCreateTime(creationTimestamp)
            .build())
    ).collect(Collectors.toList());

    BatchCreateAlertMatchesResponse response = BatchCreateAlertMatchesResponse.newBuilder()
        .addAllMatches(responseMatches)
        .build();
    return response;
  }

  private Timestamp now() {
    Instant time = Instant.now();
    return Timestamp.newBuilder().setSeconds(time.getEpochSecond())
        .setNanos(time.getNano()).build();
  }


}
