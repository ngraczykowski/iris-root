package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertSolution;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.SaveRecommendationRequest;
import com.silenteight.adjudication.engine.comments.comment.CommentFacade;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.solving.application.publisher.RecommendationPublisher;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class ResolvedAlertProcess {

  private final RecommendationPublisher recommendationPublisher;
  private final AlertSolvingRepository alertSolvingRepository;
  private final AlertSolvingAlertContextMapper alertSolvingAlertContextMapper;
  private final CommentFacade commentFacade;
  private final RecommendationFacade recommendationFacade;
  private final ProtoMessageToObjectNodeConverter converter;
  private static final String COMMENT_TEMPLATE = "alert";


  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void generateRecommendation(long alertId, BatchSolveAlertsResponse solvedAlert) {

    final AlertSolving alertSolvingModel = this.alertSolvingRepository.get(alertId);

    if (alertSolvingModel.isEmpty()) {
      return;
    }

    var alertSolution = solvedAlert.getSolutionsList().stream().findFirst();
    if (alertSolution.isEmpty()) {
      return;
    }

    var recommendedAction = alertSolution.get().getAlertSolution();
    var alertContext = alertSolvingAlertContextMapper.mapSolvingAlert(
        alertSolvingModel,
        recommendedAction);
    var comment = commentFacade.generateComment(COMMENT_TEMPLATE, alertContext);

    var saveRequest =
        new SaveRecommendationRequest(alertSolvingModel.getAnalysisId(), true, true, List.of(
            AlertSolution
                .builder()
                .alertId(alertSolvingModel.getAlertId())
                .recommendedAction(recommendedAction)
                .matchIds(alertSolvingModel.getMatchIds())
                .matchContexts(createMatchContexts(alertContext.getMatches()))
                .comment(comment)
                .matchComments(Map.of())
                .build()));
    var recommendations = recommendationFacade.createRecommendations(saveRequest);

    sendRecommendationNotification(alertSolvingModel, recommendations);
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
        .map(match -> converter.convert(match).get())
        .toArray(ObjectNode[]::new);
  }
}
