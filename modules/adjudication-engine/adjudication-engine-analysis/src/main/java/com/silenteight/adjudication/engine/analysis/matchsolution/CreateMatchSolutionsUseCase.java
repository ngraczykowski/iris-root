package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchSolution;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchSolutionCollection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
@RequiredArgsConstructor
class CreateMatchSolutionsUseCase {

  private final MatchSolutionRepository matchSolutionRepository;

  @Setter(onMethod_ = @Autowired)
  private ObjectMapper objectMapper = new ObjectMapper();

  @Transactional
  void createMatchSolutions(MatchSolutionCollection collection) {
    collection
        .getMatchSolutions()
        .stream()
        .map(m -> makeEntity(collection.getAnalysisId(), m))
        .forEach(matchSolutionRepository::save);
  }

  private MatchSolutionEntity makeEntity(long analysisId, MatchSolution matchSolution) {
    var solutionResponse = matchSolution.getResponse();

    var builder = MatchSolutionEntity.builder()
        .analysisId(analysisId)
        .matchId(matchSolution.getMatchId())
        .solution(solutionResponse.getFeatureVectorSolution().toString());

    var featureVectorSignature = Base64
        .getEncoder()
        .encodeToString(solutionResponse.getFeatureVectorSignature().toByteArray());

    var nodeFactory = objectMapper.getNodeFactory();
    var reason = nodeFactory.objectNode();

    reason.set("featureVectorSignature", nodeFactory.textNode(featureVectorSignature));
    reason.set("stepId", nodeFactory.textNode(solutionResponse.getStepId().toString()));

    return builder.reason(reason).build();
  }
}
