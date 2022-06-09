package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertSolution;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.SaveRecommendationRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.port.RecommendationFacadePort;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.comments.comment.port.CommentFacadePort;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.solving.application.process.port.SolvedAlertPort;
import com.silenteight.adjudication.engine.solving.application.publisher.RecommendationPublisher;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInputClientRepository;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Slf4j
class SolvedAlertProcess implements SolvedAlertPort {

  private final RecommendationPublisher recommendationPublisher;
  private final AlertSolvingRepository alertSolvingRepository;
  private final AlertSolvingAlertContextMapper alertSolvingAlertContextMapper;
  private final CommentFacadePort commentFacadePort;
  private final RecommendationFacadePort recommendationFacadePort;
  private final ProtoMessageToObjectNodeConverter converter;
  private final CommentInputClientRepository commentInputClientRepository;
  private final ProcessConfigurationProperties.SolvedAlertProcess properties;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void generateRecommendation(long alertId, BatchSolveAlertsResponse solvedAlert) {

    log.info("Generating recommendation for = {}", alertId);

    final AlertSolving alertSolvingModel = this.alertSolvingRepository.get(alertId);

    if (alertSolvingModel.isEmpty()) {
      return;
    }

    var alertSolution = solvedAlert.getSolutionsList().stream().findFirst();
    if (alertSolution.isEmpty()) {
      return;
    }

    var recommendedAction = alertSolution.get().getAlertSolution();
    final Map<String, Object> commentsInputs = this.fetchCommentInputs(alertId);
    var alertContext = alertSolvingAlertContextMapper.mapSolvingAlert(
        alertSolvingModel,
        recommendedAction,
        commentsInputs
    );
    var comment = commentFacadePort.generateComment(properties.getCommentTemplate(), alertContext);
    var matchComments =
        commentFacadePort.generateMatchComments(
            properties.getMatchCommentTemplate(), alertContext.getMatches());

    var saveRequest =
        new SaveRecommendationRequest(alertSolvingModel.getAnalysisId(), true, true, true, List.of(
            AlertSolution
                .builder()
                .alertId(alertSolvingModel.getAlertId())
                .recommendedAction(recommendedAction)
                .matchIds(alertSolvingModel.getMatchIds())
                .matchContexts(createMatchContexts(alertContext.getMatches()))
                .comment(comment)
                .matchComments(matchComments)
                .alertLabels(converter.convert(alertSolvingModel.getLabels()))
                .build()));
    var recommendations = recommendationFacadePort.createRecommendations(saveRequest);

    log.debug("Generated recommendation for = {}", alertSolvingModel.getAlertId());

    sendRecommendationNotification(alertSolvingModel, recommendations);
  }

  private Map<String, Object> fetchCommentInputs(long alertId) {
    return this.commentInputClientRepository.get(alertId);

  }

  private void sendRecommendationNotification(
      AlertSolving alertSolvingModel,
      List<RecommendationsGenerated.RecommendationInfo> recommendations) {
    RecommendationsGenerated recommendationsGenerated =
        createRecommendationsNotification(alertSolvingModel, recommendations);

    this.recommendationPublisher.publish(recommendationsGenerated);
  }

  @Nonnull
  private RecommendationsGenerated createRecommendationsNotification(
      AlertSolving alertSolvingModel,
      List<RecommendationsGenerated.RecommendationInfo> recommendations) {
    return RecommendationsGenerated.newBuilder()
        .setAnalysis(alertSolvingModel.getAnalysisName())
        .addAllRecommendationInfos(recommendations)
        .build();
  }

  private ObjectNode[] createMatchContexts(List<MatchContext> matchContexts) {
    return matchContexts
        .stream()
        .map(converter::convert)
        .filter(not(ObjectNode::isEmpty))
        .toArray(ObjectNode[]::new);
  }
}
