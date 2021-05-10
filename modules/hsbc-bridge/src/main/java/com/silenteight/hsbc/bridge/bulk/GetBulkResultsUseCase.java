package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.bulk.exception.BulkProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.SolvedAlertStatus;
import com.silenteight.hsbc.bridge.bulk.rest.BulkSolvedAlertsResponse;
import com.silenteight.hsbc.bridge.recommendation.RecommendationFacade;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.COMPLETED;

@RequiredArgsConstructor
@Slf4j
public class GetBulkResultsUseCase {

  private final AlertFacade alertFacade;
  private final BulkRepository bulkRepository;
  private final RecommendationFacade recommendationFacade;

  public BulkSolvedAlertsResponse getResults(String id) {
    var bulk = bulkRepository.findById(id);

    if (bulk.getStatus() != COMPLETED) {
      throw new BulkProcessingNotCompletedException(id);
    }

    var response = new BulkSolvedAlertsResponse();
    response.setBulkId(bulk.getId());
    response.setBulkStatus(com.silenteight.hsbc.bridge.bulk.rest.BulkStatus.valueOf(
        bulk.getStatus().name()));
    //response.setAlerts(getSolvedAlerts(bulk.getItems()));
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
/*
  private List<SolvedAlert> getSolvedAlerts(Collection<BulkItem> items) {
    return items.stream().map(bulkItem -> {
      var recommendation = getRecommendation(bulkItem.getId());

      var solvedAlert = new SolvedAlert();
      solvedAlert.setId(bulkItem.getAlertExternalId());
      solvedAlert.setRecommendation(mapRecommendation(recommendation.getRecommendedAction()));
      solvedAlert.setComment(recommendation.getRecommendationComment());
      return solvedAlert;
    }).collect(Collectors.toList());
  }

  private RecommendationDto getRecommendation(long bulkItemId) {
    var alert = alertFacade.getAlertNameByBulkId(bulkItemId);
    return recommendationFacade.getRecommendation(alert);
  }*/
}
