package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.FeatureContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.Match;
import com.silenteight.adjudication.engine.solving.domain.MatchFeature;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertSolvingAlertContextMapper {

  private final ProtoMessageToObjectNodeConverter converter;

  public AlertContext mapSolvingAlert(AlertSolving alertSolving, String recommendedAction,
      Map<String, Object> commentInputs) {
    return AlertContext
        .builder()
        .alertId(alertSolving.getAlertName())
        .recommendedAction(recommendedAction)
        .matches(mapMatches(alertSolving.getMatches()))
        .commentInput(commentInputs)
        .build();
  }

  private List<MatchContext> mapMatches(Map<Long, Match> matches) {
    return matches.values().stream().map(this::mapMatch).collect(toList());
  }

  private MatchContext mapMatch(Match match) {

    return MatchContext
        .builder()
        .matchId(match.getClientMatchId())
        .solution(match.getSolution())
        .reason(converter.convertJsonToMap(match.getReason()).orElse(Map.of()))
        .features(mapFeatures(match.getFeatures()))
        .build();
  }

  private Map<String, FeatureContext> mapFeatures(Map<String, MatchFeature> features) {
    return features
        .entrySet()
        .stream()
        .collect(Collectors.toMap(Entry::getKey, e -> mapFeature(e.getValue())));
  }

  private FeatureContext mapFeature(MatchFeature matchFeature) {
    return FeatureContext
        .builder()
        .solution(matchFeature.getFeatureValue())
        .agentConfig(matchFeature.getAgentConfig())
        .reason(converter.convertJsonToMap(matchFeature.getReason()).orElse(Map.of()))
        .build();
  }
}
