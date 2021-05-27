package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.exception.BulkProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.BatchSolvedAlerts;
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatus;
import com.silenteight.hsbc.bridge.bulk.rest.SolvedAlert;
import com.silenteight.hsbc.bridge.bulk.rest.SolvedAlertStatus;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationFacade;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.COMPLETED;

@RequiredArgsConstructor
@Slf4j
public class GetBulkResultsUseCase {

  private final BulkRepository bulkRepository;
  private final RecommendationFacade recommendationFacade;

  public BatchSolvedAlerts getResults(String id) {
    var bulk = bulkRepository.findById(id);

    if (bulk.getStatus() != COMPLETED) {
      throw new BulkProcessingNotCompletedException(id);
    }

    var response = new BatchSolvedAlerts();
    response.setBatchId(bulk.getId());
    response.setBatchStatus(BatchStatus.valueOf(
        bulk.getStatus().name()));
    response.setAlerts(getSolvedAlerts(bulk.getValidAlerts()));
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
      solvedAlert.setId(alert.getExternalId());

      if (alert.isCompleted()) {
        getAndFillWithRecommendation(alert.getName(), solvedAlert);
      }
      return solvedAlert;
    }).collect(Collectors.toList());
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
