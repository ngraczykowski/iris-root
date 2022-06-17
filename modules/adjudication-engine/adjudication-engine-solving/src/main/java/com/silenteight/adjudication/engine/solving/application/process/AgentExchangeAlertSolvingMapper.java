package com.silenteight.adjudication.engine.solving.application.process;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
class AgentExchangeAlertSolvingMapper {

  private final IdGenerator idGenerator = new AlternativeJdkIdGenerator();

  public List<AgentExchangeRequestMessage> from(AlertSolving alertSolving) {
    var agentFeatures = alertSolving.getAgentFeatures();
    if (log.isDebugEnabled()) {
      log.debug("Dispatch matches features {} to agents", agentFeatures.keySet());
    }
    // TODO add priority
    return agentFeatures.entrySet().stream().map(entry ->
        getFeatureExchanges(entry.getValue(), entry.getKey(), alertSolving)
    ).flatMap(List::stream).collect(Collectors.toList());
  }

  private List<AgentExchangeRequestMessage> getFeatureExchanges(
      Set<String> featureNames, String agentConfig, AlertSolving alertSolving) {
    return featureNames
        .stream()
        .map(featureName -> AgentExchangeRequestMessage.builder().agentExchangeRequest(
                AgentExchangeRequest
                    .newBuilder()
                    .addAllFeatures(List.of(featureName))
                    .addAllMatches(alertSolving.getAllMatchesNames(featureName))
                    .build())
            .agentConfig(agentConfig)
            .requestId(idGenerator.generateId())
            .priority(alertSolving.getPriority()).build())
        .filter(featureName -> featureName.getAgentExchangeRequest().getMatchesCount() > 0)
        .collect(Collectors.toList());
  }
}
