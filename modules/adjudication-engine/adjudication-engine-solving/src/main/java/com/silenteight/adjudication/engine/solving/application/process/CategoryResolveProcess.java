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

import java.util.Arrays;
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
    scheduledExecutorService.scheduleAtFixedRate(this::process, 500, 500, TimeUnit.MILLISECONDS);
  }

  public void resolve(final Long alert) {
    log.debug("Resolve category value: {}", alert);
    this.alertCategoryValuesInputQueue.add(alert);
  }

  private void process() {
    while (true) {
      final Long alertID = this.alertCategoryValuesInputQueue.poll();
      if (alertID == null) {
        break;
      }
      var alertSolving = alertSolvingRepository.get(alertID);
      var request =
          BatchGetMatchesCategoryValuesRequest.newBuilder()
              .addAllCategoryMatches(alertSolving.getCategoryMatches())
              .build();
      var categoriesValues = this.categoryValueClient.batchGetMatchCategoryValues(request);

      if (log.isDebugEnabled()) {
        log.debug("Retrieved category values: {}", categoriesValues);
      }

      categoriesValues
          .getCategoryValuesList()
          .forEach(
              cv ->
                  alertSolvingRepository.updateMatchCategoryValue(
                      alertID,
                      ResourceName.create(cv.getMatch()).getLong("matches"),
                      CategoryValue.builder()
                          .category(cv.getName())
                          .value(cv.getSingleValue())
                          .build()));

      var alert = alertSolvingRepository.get(alertID);
      Arrays.stream(alert.getMatchIds())
          .forEach(
              matchId -> {
                if (alert.isMatchReadyForSolving(matchId)) {
                  var matchSolutionRequest =
                      new MatchSolutionRequest(
                          alert.getAlertId(),
                          matchId,
                          alert.getPolicy(),
                          alert.getMatchFeatureNames(matchId),
                          alert.getMatchFeatureVectors(matchId));

                  readyMatchFeatureVectorPublisher.send(matchSolutionRequest);
                }
              });
    }
  }
}
