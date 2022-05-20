package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v3.MatchRecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputDataAccess;
import com.silenteight.adjudication.engine.analysis.matchrecommendation.domain.PendingMatch;
import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Service
@RequiredArgsConstructor
class CreateMatchRecommendationUseCase {

  private final MatchRecommendationRepository recommendationRepository;
  private final CommentInputDataAccess commentInputDataAccess;
  private final GenerateMatchCommentsUseCase generateMatchCommentsUseCase;

  private final ObjectMapper mapper;

  List<RecommendationInfo> createMatchRecommendation(
      long analysisId, List<SolveAlertSolutionResponse> solutions,
      List<PendingMatch> pendingMatches) {
    var recommendationEntities = solutions
        .stream()
        .map(s -> createMatchRecommendationEntity(s, pendingMatches, analysisId))
        .collect(toList());

    var savedEntities = recommendationRepository.saveAll(recommendationEntities);

    return getStream(savedEntities)
        .map(entity -> entity.toRecommendationInfo(
            mapper.convertValue(
                getPendingMatch(pendingMatches, entity.getMatchId()).getMatchContexts(),
                MatchContext.class)))
        .collect(
            toList());
  }

  private MatchRecommendationEntity createMatchRecommendationEntity(
      SolveAlertSolutionResponse response, List<PendingMatch> pendingMatches, long analysisId) {

    var matchId = ResourceName.create(response.getAlertName()).getLong("matches");
    var alertId = ResourceName.create(response.getAlertName()).getLong("alerts");
    var pendingMatch = getPendingMatch(pendingMatches, matchId);
    var comment =
        generateComment(
            response.getAlertSolution(), pendingMatch.getAlertId(),
            pendingMatch.getMatchContexts());

    return MatchRecommendationEntity
        .builder()
        .matchId(matchId)
        .alertId(alertId)
        .analysisId(analysisId)
        .comment(comment)
        .recommendedAction(response.getAlertSolution())
        .build();
  }

  private static PendingMatch getPendingMatch(List<PendingMatch> pendingMatches, long matchId) {
    return pendingMatches
        .stream()
        .filter(m -> m.getMatchId() == matchId)
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException(
            "Couldn't match pending match with received solved matches"));
  }

  private String generateComment(
      String alertSolution, long alertId, ObjectNode matchContext) {

    var commentInput =
        commentInputDataAccess.getCommentInputByAlertId(alertId).orElse(Collections.emptyMap());

    var alertContext = AlertContext.builder()
        .alertId(String.valueOf(alertId))
        .commentInput(commentInput)
        .recommendedAction(alertSolution)
        .matches(List.of(mapper.convertValue(matchContext, MatchContext.class)))
        .build();

    return generateMatchCommentsUseCase.generateComments(alertContext);
  }

  @Nonnull
  private static <T> Stream<T> getStream(Iterable<T> m) {
    return stream(m.spliterator(), false);
  }
}
