package com.silenteight.bridge.core.reports.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.reports.domain.model.AlertWithMatchesDto;
import com.silenteight.bridge.core.reports.domain.model.RecommendationWithMetadataDto;
import com.silenteight.bridge.core.reports.domain.model.Report;
import com.silenteight.bridge.core.reports.domain.port.outgoing.RecommendationService;
import com.silenteight.bridge.core.reports.domain.port.outgoing.RegistrationService;
import com.silenteight.bridge.core.reports.domain.port.outgoing.ReportsSenderService;

import org.springframework.stereotype.Service;

import java.util.Optional;
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

  void sendReports(String batchId, String analysisName) {
    var alertNameToRecommendation = recommendationService.getRecommendations(analysisName).stream()
        .collect(Collectors.toMap(RecommendationWithMetadataDto::alertName, Function.identity()));

    var reports = registrationService.getAlertsWithMatches(batchId).stream()
        .map(alert -> toReport(batchId, alert, alertNameToRecommendation.get(alert.name())))
        .toList();

    reportsSenderService.send(analysisName, reports);
    log.info("Sent reports for batch {} (analysis [{}])", batchId, analysisName);
  }

  private Report toReport(
      String batchId, AlertWithMatchesDto alert, RecommendationWithMetadataDto recommendation) {
    return Optional.ofNullable(recommendation)
        .map(rec -> reportsMapper.toReport(batchId, alert, recommendation))
        .orElseGet(() -> reportsMapper.toErroneousReport(batchId, alert));
  }
}
