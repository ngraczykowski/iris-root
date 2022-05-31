package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.CategoryValuesClient;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.application.publisher.ReadyMatchFeatureVectorPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.CategoryValue;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;

import com.hazelcast.collection.IQueue;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
class CategoryResolveProcess {

  private final CategoryValuesClient categoryValueClient;
  private final AlertSolvingRepository alertSolvingRepository;
  private final IQueue<Long> alertCategoryValuesInputQueue;
  private final ReadyMatchFeatureVectorPublisher readyMatchFeatureVectorPublisher;

  CategoryResolveProcess(
      final CategoryValuesClient categoryValueClient,
      final ScheduledExecutorService scheduledExecutorService,
      final IQueue<Long> alertCommentsInputQueue,
      final AlertSolvingRepository alertSolvingRepository,
      final ReadyMatchFeatureVectorPublisher readyMatchFeatureVectorPublisher) {
    this.categoryValueClient = categoryValueClient;
    this.alertCategoryValuesInputQueue = alertCommentsInputQueue;
    this.alertSolvingRepository = alertSolvingRepository;
    this.readyMatchFeatureVectorPublisher = readyMatchFeatureVectorPublisher;
    scheduledExecutorService.scheduleAtFixedRate(this::process, 10, 10, TimeUnit.MILLISECONDS);
  }

  public void resolve(final Long alertId) {
    log.debug("Resolve category value for alert: {}", alertId);
    this.alertCategoryValuesInputQueue.add(alertId);
  }

  private void process() {
    try {
      execute();
    } catch (Exception e) {
      log.error("Processing of category value failed: ", e);
    }
  }

  private void execute() {
    while (true) {
      final Long alertId = this.alertCategoryValuesInputQueue.poll();
      if (alertId == null) {
        break;
      }
      resolveCategoryValues(alertId);
    }
  }

  private void resolveCategoryValues(Long alertId) {
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
