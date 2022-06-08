package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.CategoryValuesClient;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.application.process.port.CategoryResolveProcessPort;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.port.ReadyMatchFeatureVectorPort;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.CategoryValue;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;

@Slf4j
@RequiredArgsConstructor
class CategoryResolveProcess implements CategoryResolveProcessPort {

  private final CategoryValuesClient categoryValueClient;
  private final AlertSolvingRepository alertSolvingRepository;
  private final ReadyMatchFeatureVectorPort readyMatchFeatureVectorPublisher;

  public void resolveCategoryValues(Long alertId) {
    log.debug("Resolved alert {} for requesting for category value", alertId);
    var alertSolving = alertSolvingRepository.get(alertId);
    var categoryMatches = alertSolving.getCategoryMatches();

    if (categoryMatches.isEmpty()) {
      log.debug("Alert {} doesn't have categories to request", alertId);
      return;
    }

    var request =
        BatchGetMatchesCategoryValuesRequest.newBuilder()
            .addAllCategoryMatches(categoryMatches)
            .build();
    var categoriesValues = this.categoryValueClient.batchGetMatchCategoryValues(request);

    log.debug("Retrieved category values: {}", categoriesValues);

    for (var cv : categoriesValues.getCategoryValuesList()) {
      alertSolvingRepository.updateMatchCategoryValue(
          alertId,
          ResourceName.create(cv.getMatch()).getLong("matches"),
          CategoryValue.builder().category(cv.getName()).value(cv.getSingleValue()).build());
    }

    var alert = alertSolvingRepository.get(alertId);

    for (var matchId : alert.getMatchIds()) {
      if (!alert.isMatchReadyForSolving(matchId)) {
        continue;
      }

      var matchSolutionRequest =
          new MatchSolutionRequest(
              alert.getAlertId(),
              matchId,
              alert.getPolicy(),
              alert.getMatchFeatureNames(matchId),
              alert.getMatchFeatureVectors(matchId));

      readyMatchFeatureVectorPublisher.send(matchSolutionRequest);
    }
  }
}
