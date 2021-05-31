package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.*;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationFacade;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class GetBulkResultsUseCase {

  private final BulkRepository bulkRepository;
  private final RecommendationFacade recommendationFacade;

  public BatchSolvedAlerts getResults(String id) {
    var result = bulkRepository.findById(id);

    if (result.isEmpty()) {
      throw new BatchIdNotFoundException(id);
    }

    var batch = result.get();
    if (batch.isNotCompleted()) {
      throw new BatchProcessingNotCompletedException(id);
    }

    var response = new BatchSolvedAlerts();
    response.setBatchId(batch.getId());
    response.setBatchStatus(BatchStatus.valueOf(
        batch.getStatus().name()));
    response.setAlerts(getSolvedAlerts(batch.getValidAlerts()));
    return response;
  }

  private SolvedAlertStatus mapRecommendation(String recommendedAction) {
    switch (recommendedAction) {
      case "FALSE_POSITIVE":
        return SolvedAlertStatus.FALSE_POSITIVE;
      case "POTENTIAL_TRUE_POSITIVE":
        return SolvedAlertStatus.POTENTIAL_TRUE_POSITIVE;
      default:
        return SolvedAlertStatus.MANUAL_INVESTIGATION;
    }
  }

  private List<SolvedAlert> getSolvedAlerts(Collection<BulkAlertEntity> items) {
    return items.stream().map(alert -> {
      var solvedAlert = new SolvedAlert();
      solvedAlert.setAlertMetadata(getAlertMetadata(alert.getExternalId()));
      solvedAlert.setId(alert.getExternalId());

      if (alert.isCompleted()) {
        getAndFillWithRecommendation(alert.getName(), solvedAlert);
      }
      return solvedAlert;
    }).collect(Collectors.toList());
  }

  private List<AlertMetadata> getAlertMetadata(String alertId) {
    var metadata = new ArrayList<AlertMetadata>();
    //TODO determine how to get proper metadata
    var trackingMetadata = new AlertMetadata();
    trackingMetadata.setKey("trackingId");
    trackingMetadata.setValue(alertId);

    var policyMetadata = new AlertMetadata();
    policyMetadata.setKey("policy_id");
    policyMetadata.setValue(UUID.randomUUID().toString());

    metadata.add(trackingMetadata);
    metadata.add(policyMetadata);
    return metadata;
  }

  private void getAndFillWithRecommendation(String alert, SolvedAlert solvedAlert) {
    var recommendation = getRecommendation(alert);
    solvedAlert.setRecommendation(mapRecommendation(recommendation.getRecommendedAction()));
    solvedAlert.setComment(recommendation.getRecommendationComment());
  }

  private RecommendationDto getRecommendation(String alert) {
    return recommendationFacade.getRecommendation(alert);
  }
}
