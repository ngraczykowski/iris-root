package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchSolution;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchSolutionCollection;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SaveMatchSolutionRequest;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.proto.protobuf.Uuid;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("FeatureEnvy")
class CreateMatchSolutionsUseCase {

  private final MatchSolutionDataAccess matchSolutionDataAccess;
  private final ProtoMessageToObjectNodeConverter protoMessageToObjectNodeConverter;

  @Setter(onMethod_ = @Autowired)
  private ObjectMapper objectMapper = new ObjectMapper();

  @Transactional
  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "matchsolution" })
  void createMatchSolutions(MatchSolutionCollection collection) {
    var requests = collection
        .getMatchSolutions()
        .stream()
        .map(m -> makeEntity(collection.getAnalysisId(), m))
        .collect(toList());
    matchSolutionDataAccess.save(requests);
  }

  private SaveMatchSolutionRequest makeEntity(long analysisId, MatchSolution matchSolution) {
    var solutionResponse = matchSolution.getResponse();

    var builder = SaveMatchSolutionRequest.builder()
        .analysisId(analysisId)
        .matchId(matchSolution.getMatchId())
        .solution(solutionResponse.getFeatureVectorSolution().toString());

    if (!solutionResponse.hasReason()) {
      builder.reason(makeDeprecatedReason(
          solutionResponse.getFeatureVectorSignature(), solutionResponse.getStepId()));
    } else {
      protoMessageToObjectNodeConverter.convert(solutionResponse.getReason())
          .ifPresentOrElse(builder::reason, () -> log.error(
              "Failed to convert the solution reason to JSON: response={}",
              solutionResponse));
    }

    return builder.build();
  }

  private ObjectNode makeDeprecatedReason(ByteString featureVectorSignature, Uuid stepId) {

    var featureVectorSignatureBase = Base64
        .getEncoder()
        .encodeToString(featureVectorSignature.toByteArray());
    var nodeFactory = objectMapper.getNodeFactory();
    var reason = nodeFactory.objectNode();

    reason.set("featureVectorSignature", nodeFactory.textNode(featureVectorSignatureBase));
    reason.set("stepId", nodeFactory.textNode(stepId.toString()));

    return reason;
  }
}
