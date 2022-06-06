package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationCommand;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedBatchTimeoutCommand;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedDataRetentionOnRecommendationsCommand;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedReadyRecommendationsCommand;
import com.silenteight.bridge.core.recommendation.domain.model.BatchStatistics;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationDto;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RegistrationService;
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.AlertsStreamProperties;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.MatchWithAlertId;
import com.silenteight.bridge.core.registration.domain.model.BatchIdWithPolicy;
import com.silenteight.proto.recommendation.api.v1.RecommendationResponse;
import com.silenteight.proto.recommendation.api.v1.RecommendationsResponse;

import io.grpc.stub.StreamObserver;
import one.util.streamex.StreamEx;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationFacade {

  private final RegistrationService registrationService;
  private final RecommendationProcessor recommendationProcessor;
  private final RecommendationRepository recommendationRepository;
  private final BatchStatisticsService batchStatisticsService;
  private final AlertsStreamProperties streamProperties;
  private final RecommendationsDataRetentionService dataRetentionService;

  public void proceedReadyRecommendations(ProceedReadyRecommendationsCommand command) {
    var batchPriority = registrationService.getBatchPriority(command.analysisName());
    recommendationProcessor.proceedReadyRecommendations(command, batchPriority.priority());
  }

  public void proceedBatchTimeout(ProceedBatchTimeoutCommand command) {
    var batchPriority = registrationService.getBatchPriority(command.analysisName());
    recommendationProcessor.createTimedOutRecommendations(command, batchPriority.priority());
  }

  public RecommendationsResponse getRecommendationsResponse(GetRecommendationCommand command) {
    var batchWithAlerts =
        registrationService.getBatchWithAlerts(command.analysisName(), command.alertNames());
    var recommendations = getRecommendations(command);
    var batchStatistics = batchStatisticsService.createBatchStatistics(
        batchWithAlerts.alerts(),
        recommendations);

    return RecommendationMapper.toRecommendationsResponse(
        batchWithAlerts,
        recommendations,
        OffsetDateTime.now(),
        batchStatistics);
  }

  public List<RecommendationWithMetadata> getRecommendations(GetRecommendationCommand command) {
    if (CollectionUtils.isEmpty(command.alertNames())) {
      return recommendationRepository.findByAnalysisName(command.analysisName());
    }
    return recommendationRepository.findByAnalysisNameAndAlertNameIn(
        command.analysisName(), command.alertNames());
  }

  public void streamRecommendationResponses(
      GetRecommendationCommand command,
      StreamObserver<RecommendationResponse> responseObserver) {
    var batchId = registrationService.getBatchId(command.analysisName());
    var alertsStream = registrationService.streamAllAlerts(batchId.id(), command.alertNames());
    var statistics = batchStatisticsService.createBatchStatistics(
        batchId.id(),
        command.analysisName(),
        command.alertNames());

    var counter = new AtomicInteger(0);

    try (var streamEx = StreamEx.of(alertsStream)) {
      streamEx.groupRuns((prev, next) -> counter.incrementAndGet()
              % streamProperties.registrationApiToRecommendationAlertsChunkSize() != 0)
          .forEach(
              alerts -> processRecommendations(
                  alerts, batchId, statistics, command, responseObserver));
    }

    responseObserver.onCompleted();
  }

  public void performDataRetention(ProceedDataRetentionOnRecommendationsCommand command) {
    dataRetentionService.performDataRetention(command);
  }

  private void processRecommendations(
      List<AlertWithoutMatches> alerts,
      BatchIdWithPolicy batchId,
      BatchStatistics statistics,
      GetRecommendationCommand command,
      StreamObserver<RecommendationResponse> responseObserver) {
    var ids = extractAlertsIds(alerts);

    var names = alerts.stream()
        .map(AlertWithoutMatches::alertName)
        .filter(Objects::nonNull)
        .toList();

    var matches = registrationService.getAllMatchesForAlerts(ids)
        .stream()
        .collect(Collectors.groupingBy(MatchWithAlertId::alertId));

    var recommendations = getRecommendations(command.analysisName(), names);

    alerts.forEach(alert -> {
      var recommendationResponse = RecommendationMapper.toRecommendationResponse(
          RecommendationDto.builder()
              .batchId(batchId)
              .alert(alert)
              .matchWithAlertIds(matches.get(alert.id()))
              .recommendation(recommendations.getOrDefault(alert.alertName(), null))
              .recommendedAtForErrorAlerts(OffsetDateTime.now())
              .statistics(statistics)
              .build());

      responseObserver.onNext(recommendationResponse);
    });
  }

  private static Set<Long> extractAlertsIds(List<AlertWithoutMatches> alerts) {
    return alerts.stream()
        .map(alert -> Long.valueOf(alert.id()))
        .collect(Collectors.toSet());
  }

  private Map<String, RecommendationWithMetadata> getRecommendations(
      String analysisName,
      List<String> alertNames) {
    if (alertNames.isEmpty()) {
      return Collections.emptyMap();
    }

    return recommendationRepository.findByAnalysisNameAndAlertNameIn(
            analysisName,
            alertNames)
        .stream()
        .collect(Collectors.toMap(RecommendationWithMetadata::alertName, Function.identity()));
  }
}
