package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchSolution;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchSolutionCollection;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.solving.api.v1.SolutionResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
class CreateMatchSolutionsUseCase {

  private final MatchSolutionRepository matchSolutionRepository;
  private final ProtoMessageToObjectNodeConverter protoMessageToObjectNodeConverter;

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

    if (!solutionResponse.hasReason()) {
      var reason =
          makeDeprecatedReason(solutionResponse, solutionResponse.getFeatureVectorSignature());
      builder.reason(reason);
    } else {
      protoMessageToObjectNodeConverter.convert(solutionResponse.getReason())
          .ifPresentOrElse(builder::reason, () -> log.error(
              "Failed to convert the solution reason to JSON: response={}",
              solutionResponse));
    }

    return builder.build();
  }

  private ObjectNode makeDeprecatedReason(
      SolutionResponse solutionResponse, ByteString featureVectorSignature) {

    var featureVectorSignatureBase = Base64
        .getEncoder()
        .encodeToString(featureVectorSignature.toByteArray());
    var nodeFactory = objectMapper.getNodeFactory();
    var reason = nodeFactory.objectNode();

    reason.set("featureVectorSignature", nodeFactory.textNode(featureVectorSignatureBase));
    reason.set("stepId", nodeFactory.textNode(solutionResponse.getStepId().toString()));

    return reason;
  }
}
