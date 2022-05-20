package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.*;
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
    var matchContext = createMatchContext(matchSolution);

    var builder = SaveMatchSolutionRequest.builder()
        .analysisId(analysisId)
        .matchId(matchSolution.getMatchId())
        .solution(matchContext.getSolution())
        .reason(matchContext.getReason())
        .context(objectMapper.convertValue(matchContext, ObjectNode.class));

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

  private MatchContext createMatchContext(MatchSolution matchSolution) {
    var solutionResponse = matchSolution.getResponse();

    var builder = MatchContext.builder()
        .matchId(matchSolution.getClientMatchIdentifier())
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

    matchSolution.getCategories().forEach(c -> builder.category(c.getName(), c.getValue()));

    matchSolution.getFeatures().forEach(f ->
        builder.feature(f.getName(), FeatureContext.builder()
            .solution(f.getValue())
            .reason(f.getReason())
            .agentConfig(f.getAgentConfig())
            .build()));

    return builder.build();
  }
}
