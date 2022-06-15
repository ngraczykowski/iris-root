/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.solving.domain.MatchSolution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public final class MatchSolutionEntityExtractor {
  private final ProtoMessageToObjectNodeConverter converter;
  private final ObjectMapper objectMapper;

  public MatchSolutionEntity extract(MatchSolution matchSolution) {
    return new MatchSolutionEntity(
        matchSolution.analysisId(),
        matchSolution.matchId(),
        matchSolution.solution(),
        this.reason(matchSolution),
        this.matchContext(matchSolution));
  }

  private String reason(MatchSolution matchSolution) {
    return objectMapper.convertValue(getMapFromReason(matchSolution.reason()), ObjectNode.class)
        .toString();
  }

  private String matchContext(MatchSolution matchSolution) {
    record FeatureContext(String solution, Map<String, Object> reason, String agentConfig) {
    }

    record SolutionContext(String matchId, Map<String, Object> reason, String solution,
        Map<String, FeatureContext> features, Map<String, String> categories) {
    }

    var features =
        matchSolution.features().entrySet().stream()
            .collect(
                toMap(
                    Map.Entry::getKey,
                    e -> new FeatureContext(
                            e.getValue().getFeatureValue(),
                            getMapFromReason(e.getValue().getReason()),
                            e.getValue().getAgentConfig())));
    var categories =
        matchSolution.categories().entrySet().stream()
            .collect(toMap(Map.Entry::getKey, e -> e.getValue().getCategoryValue()));

    return objectMapper
        .convertValue(
            new SolutionContext(
                matchSolution.clientMatchId(),
                getMapFromReason(matchSolution.reason()),
                matchSolution.solution(),
                features,
                categories),
            ObjectNode.class)
        .toString();
  }

  private Map<String, Object> getMapFromReason(String reason) {
    return converter.convertJsonToMap(reason).orElse(Map.of());
  }
}
