package com.silenteight.serp.governance.qa.sampling.generator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertDistribution;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.Distribution;
import com.silenteight.serp.governance.qa.manage.analysis.create.CreateAlertWithDecisionUseCase;
import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;
import com.silenteight.serp.governance.qa.sampling.domain.AlertSamplingService;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.generator.dto.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.NEW;
import static java.time.OffsetDateTime.now;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class AlertsGeneratorService {

  @NonNull
  private final DistributionProvider distributionProvider;
  @NonNull
  private final AlertProvider alertProvider;
  @NonNull
  private final CreateAlertWithDecisionUseCase createAlertWithDecisionUseCase;
  private final long totalSampleCount;
  @NonNull
  private final List<String> groupingFields;
  @NonNull
  private final List<Filter> defaultFilters;
  @NonNull
  private final AlertSamplingService alertSamplingService;
  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void generateAlerts(@Valid DateRangeDto dateRangeDto, Long alertSamplingId) {
    List<AlertDistribution> distribution = distributionProvider.getDistribution(
        dateRangeDto, groupingFields);
    long totalAlertsCount = getTotalAlertsCount(distribution);

    if (!canGenerateAlerts(totalAlertsCount)) {
      log.warn("Cannot generate alerts. Total alerts count is {}", totalAlertsCount);
      return;
    }

    log.debug("Generating alerts of total count={}", totalAlertsCount);
    DistributionCalculator distributionCalculator = getDistributionCalculator(
        totalSampleCount, totalAlertsCount);

    List<CreateDecisionRequest> createDecisionRequests = distribution.stream()
        .map(alertDistribution
            -> toAlertSampleRequest(alertDistribution, distributionCalculator, dateRangeDto))
        .map(alertProvider::getAlerts).flatMap(List::stream)
        .map(this::getCreateDecisionRequest)
        .peek(createAlertWithDecisionUseCase::activate)
        .collect(toList());

    alertSamplingService.saveAlertDistribution(
        alertSamplingId, toAlertDistributionDtoList(distribution), createDecisionRequests.size());

    eventPublisher.publishEvent(DecisionCreatedEvent.of(createDecisionRequests));
  }

  private static boolean canGenerateAlerts(long totalAlertsCount) {
    return totalAlertsCount > 0;
  }

  private static DistributionCalculator getDistributionCalculator(
      long totalSampleCount, long totalCountOverall) {

    return new DistributionCalculator(totalSampleCount, totalCountOverall);
  }

  private static long getTotalAlertsCount(List<AlertDistribution> alertDistribution) {
    return alertDistribution.stream().mapToLong(AlertDistribution::getAlertCount).sum();
  }

  private AlertsSampleRequest toAlertSampleRequest(
      AlertDistribution alertDistribution,
      DistributionCalculator distributionCalculator,
      DateRangeDto dateRangeDto) {

    Long requestedAmount = distributionCalculator
        .calculateAmount(alertDistribution.getAlertCount());

    return AlertsSampleRequest.of(
        dateRangeDto, alertDistribution.getGroupingFieldsList(), defaultFilters, requestedAmount);
  }

  private CreateDecisionRequest getCreateDecisionRequest(String alertName) {
    return CreateDecisionRequest.of(
        alertName, NEW, ANALYSIS, getClass().getSimpleName(), now());
  }

  private static List<AlertDistributionDto> toAlertDistributionDtoList(
      List<AlertDistribution> alertDistributions) {

    return alertDistributions.stream()
        .map(AlertsGeneratorService::toAlertDistributionDto)
        .collect(toList());
  }

  private static AlertDistributionDto toAlertDistributionDto(AlertDistribution alertDistribution) {
    return AlertDistributionDto.builder()
        .alertsCount(alertDistribution.getAlertCount())
        .distributions(
            toDistributionDtoList(alertDistribution.getGroupingFieldsList()))
        .build();
  }

  private static List<DistributionDto> toDistributionDtoList(List<Distribution> distributions) {
    return distributions.stream()
        .map(AlertsGeneratorService::toDistributionDto)
        .collect(toList());
  }

  private static DistributionDto toDistributionDto(Distribution distribution) {
    return DistributionDto.builder()
        .fieldName(distribution.getFieldName())
        .fieldValue(distribution.getFieldValue())
        .build();
  }
}
