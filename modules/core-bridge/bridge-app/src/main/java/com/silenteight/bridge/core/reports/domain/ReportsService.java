package com.silenteight.bridge.core.reports.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.reports.domain.model.*;
import com.silenteight.bridge.core.reports.domain.port.outgoing.RecommendationService;
import com.silenteight.bridge.core.reports.domain.port.outgoing.RegistrationService;
import com.silenteight.bridge.core.reports.domain.port.outgoing.ReportsSenderService;
import com.silenteight.bridge.core.reports.infrastructure.ReportsProperties;

import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class ReportsService {

  private final ReportsMapper reportsMapper;
  private final ReportsSenderService reportsSenderService;
  private final RegistrationService registrationService;
  private final RecommendationService recommendationService;
  private final ReportsProperties properties;

  void sendReports(String batchId, String analysisName) {
    var alertNameToRecommendation = recommendationService.getRecommendations(analysisName).stream()
        .collect(Collectors.toMap(RecommendationWithMetadataDto::alertName, Function.identity()));

    var reports = registrationService.getAlertsWithMatches(batchId).stream()
        .map(alert -> toReport(batchId, alert, alertNameToRecommendation.get(alert.name())))
        .toList();

    if (reports.isEmpty()) {
      log.info(
          "Reports for batch [{}] (analysis [{}]) are empty and will not be sent.",
          batchId, analysisName);
    } else {
      reportsSenderService.send(analysisName, reports);
      log.info("Sent reports for batch [{}] (analysis [{}])", batchId, analysisName);
    }
  }

  void streamReports(String batchId, String analysisName) {
    var alerts = registrationService.streamAlerts(batchId);
    var counter = new AtomicInteger(0);
    var numberOfMessage = new AtomicInteger(0);

    StreamEx.of(alerts)
        .groupRuns((prev, next) -> counter.incrementAndGet()
            % properties.registrationApiToReportsAlertsChunkSize() != 0)
        .forEach(alertChunk -> send(numberOfMessage, batchId, alertChunk, analysisName));
  }

  private void send(
      AtomicInteger numberOfMessage,
      String batchId,
      List<AlertWithoutMatchesDto> alerts,
      String analysisName) {
    var ids = extractAlertsIds(alerts);

    var names = alerts.stream()
        .map(AlertWithoutMatchesDto::alertName)
        .filter(Objects::nonNull)
        .toList();

    var matches = registrationService.getMatches(ids)
        .stream()
        .collect(Collectors.groupingBy(MatchWithAlertId::alertId));

    var recommendations = getRecommendations(analysisName, names);

    var reports = alerts.stream()
        .map(alert -> toReport(batchId,
            reportsMapper.toAlertWithMatches(alert, matches.get(alert.id())),
            recommendations.get(alert.alertName())))
        .toList();

    if (reports.isEmpty()) {
      log.info(
          "Reports stream for batch [{}] (analysis [{}]) are empty and will not be sent."
              + " Number of chunk [{}]",
          batchId, analysisName, numberOfMessage.incrementAndGet());
    } else {
      reportsSenderService.send(analysisName, reports);
      log.info("Sent reports for batch [{}] (analysis [{}]). Number of chunk [{}]",
          batchId, analysisName, numberOfMessage.incrementAndGet());
    }
  }

  private Report toReport(
      String batchId, AlertWithMatchesDto alert, RecommendationWithMetadataDto recommendation) {
    return Optional.ofNullable(recommendation)
        .map(rec -> reportsMapper.toReport(batchId, alert, recommendation))
        .orElseGet(() -> reportsMapper.toErroneousReport(batchId, alert));
  }

  private static Set<Long> extractAlertsIds(List<AlertWithoutMatchesDto> alerts) {
    return alerts.stream()
        .map(alert -> Long.valueOf(alert.id()))
        .collect(Collectors.toSet());
  }

  private Map<String, RecommendationWithMetadataDto> getRecommendations(
      String analysisName,
      List<String> alertNames) {
    if (alertNames.isEmpty()) {
      return Collections.emptyMap();
    }

    return recommendationService.getRecommendations(
            analysisName,
            alertNames)
        .stream()
        .collect(Collectors.toMap(RecommendationWithMetadataDto::alertName, Function.identity()));
  }
}
