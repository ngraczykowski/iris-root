package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputDataAccess;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.*;
import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("FeatureEnvy")
@Service
@Slf4j
class GenerateRecommendationsUseCase {

  private final GovernanceFacade governanceFacade;
  private final RecommendationDataAccess recommendationDataAccess;
  private final AnalysisFacade analysisFacade;
  private final GenerateCommentsUseCase generateCommentsUseCase;
  private final CommentInputDataAccess commentInputDataAccess;
  private final CreateRecommendationsUseCase createRecommendationsUseCase;
  private final Counter invocationLoopCounter;
  private final AtomicLong recommendationInfoSize;

  GenerateRecommendationsUseCase(
      final GovernanceFacade governanceFacade,
      final RecommendationDataAccess recommendationDataAccess,
      final AnalysisFacade analysisFacade,
      final GenerateCommentsUseCase generateCommentsUseCase,
      final CommentInputDataAccess commentInputDataAccess,
      final CreateRecommendationsUseCase createRecommendationsUseCase,
      final MeterRegistry meterRegistry
  ) {
    this.governanceFacade = governanceFacade;
    this.recommendationDataAccess = recommendationDataAccess;
    this.analysisFacade = analysisFacade;
    this.generateCommentsUseCase = generateCommentsUseCase;
    this.commentInputDataAccess = commentInputDataAccess;
    this.createRecommendationsUseCase = createRecommendationsUseCase;
    this.invocationLoopCounter = meterRegistry.counter("prepare.recommendation.info.invocation");
    this.recommendationInfoSize =
        meterRegistry.gauge("recommendation.info.size", new AtomicLong(0));
  }

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "recommendation" })
  List<RecommendationInfo> generateRecommendations(String analysisName) {

    var analysisId = ResourceName.create(analysisName).getLong("analysis");
    log.debug("Starting generating recommendations: analysis={}", analysisName);
    var recommendationInfos =
        prepareRecommendationInfos(analysisName, analysisId);

    log.info("Finished generating recommendations: analysis={}, recommendationCount={}",
        analysisName, recommendationInfos.size());

    return recommendationInfos;
  }

  private List<RecommendationInfo> prepareRecommendationInfos(
      String analysisName,
      long analysisId
  ) {
    var recommendationInfos = new LinkedList<RecommendationInfo>();
    var analysisAttachmentFlags = this.analysisFacade.getAnalysisAttachmentFlags(analysisName);

    do {
      this.invocationLoopCounter.increment();
      var pendingAlerts = recommendationDataAccess.selectPendingAlerts(analysisId);
      var request = createRequest(analysisId, pendingAlerts);

      if (request.isEmpty()) {
        debug("No more alerts pending recommendation: analysis={}", analysisName);
        break;
      }

      var response = governanceFacade.batchSolveAlerts(request.get());

      var alertSolutions = createAlertSolutions(pendingAlerts, response);

      recommendationInfos.addAll(
          createRecommendationsUseCase.createRecommendations(
              new SaveRecommendationRequest(analysisId, analysisAttachmentFlags.isAttachMetadata(),
                  analysisAttachmentFlags.isAttachRecommendation(),false,
                  alertSolutions)));

      if (alertSolutions.isEmpty()) {
        log.info("No recommendations generated: analysis={}", analysisName);
      }

    } while (true);

    this.recommendationInfoSize.set(recommendationInfos.size());
    return recommendationInfos;
  }

  @NotNull
  private List<AlertSolution> createAlertSolutions(
      PendingAlerts alerts, BatchSolveAlertsResponse response) {

    return response
        .getSolutionsList()
        .stream()
        .map(solveAlertSolutionResponse -> createAlertSolution(alerts, solveAlertSolutionResponse))
        .collect(toList());
  }

  private Optional<BatchSolveAlertsRequest> createRequest(
      long analysisId, PendingAlerts pendingAlerts) {

    if (pendingAlerts.isEmpty()) {
      log.debug("No pending alerts: analysis=analysis/{}", analysisId);
      return Optional.empty();
    }

    debug("Fetched pending alerts: analysis=analysis/{}, pendingCount={}",
        analysisId, pendingAlerts.size());

    var strategy = analysisFacade.getAnalysisStrategy(analysisId);
    return Optional.of(pendingAlerts.toBatchSolveAlertsRequest(strategy));
  }

  private AlertSolution createAlertSolution(
      PendingAlerts alerts, SolveAlertSolutionResponse response) {

    var alertId = ResourceName.create(response.getAlertName()).getLong("alerts");
    var pendingAlert = getPendingAlert(alerts, alertId);
    var comment =
        generateComment(response.getAlertSolution(), alertId, pendingAlert.getMatchContexts());
    var matchComments = generateMatchComments(pendingAlert.getMatchContexts());

    return AlertSolution.builder()
        .alertId(alertId)
        .recommendedAction(response.getAlertSolution())
        .matchIds(pendingAlert.getMatchIds())
        .matchContexts(pendingAlert.getMatchContexts())
        .comment(comment)
        .matchComments(matchComments)
        .alertLabels(pendingAlert.getAlertLabels())
        .build();
  }

  private Map<String, String> generateMatchComments(ObjectNode[] matchContexts) {
    return generateCommentsUseCase.generateMatchComments(generateMatchContexts(matchContexts));
  }

  private static void debug(final String message, final Object... parameters) {
    if (log.isDebugEnabled()) {
      log.debug(message, parameters);
    }
  }

  private String generateComment(
      String alertSolution, long alertId, ObjectNode[] matchContexts) {

    var commentInput =
        commentInputDataAccess.getCommentInputByAlertId(alertId).orElse(Collections.emptyMap());

    var generateCommentsRequest = new GenerateCommentsRequest(AlertContext.builder()
        .alertId(String.valueOf(alertId))
        .commentInput(commentInput)
        .recommendedAction(alertSolution)
        .matches(generateMatchContexts(matchContexts))
        .build());

    return generateCommentsUseCase
        .generateComments(generateCommentsRequest)
        .getComment();
  }

  @Nonnull
  private static List<MatchContext> generateMatchContexts(ObjectNode[] matchContextsObjectNodes) {

    ObjectMapper mapper = new ObjectMapper();

    return Arrays.stream(matchContextsObjectNodes)
        .map(mc -> mapper.convertValue(mc, MatchContext.class))
        .collect(toList());
  }

  private static PendingAlert getPendingAlert(PendingAlerts alerts, long alertId) {
    return alerts.getById(alertId)
        .orElseThrow(() -> new PendingAlertSolutionNotMatchedException(alertId));
  }

  static class PendingAlertSolutionNotMatchedException extends IllegalStateException {

    private static final long serialVersionUID = -1681350458025837494L;

    PendingAlertSolutionNotMatchedException(long alertId) {
      super("Could not match Pending Alert with the Solution, alertId=" + alertId);
    }
  }
}
