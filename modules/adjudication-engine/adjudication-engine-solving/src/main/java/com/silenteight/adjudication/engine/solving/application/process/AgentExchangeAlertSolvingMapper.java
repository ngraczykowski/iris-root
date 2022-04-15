package com.silenteight.adjudication.engine.solving.application.process;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
class AgentExchangeAlertSolvingMapper {

  private IdGenerator idGenerator = new AlternativeJdkIdGenerator();

  public List<AgentExchangeRequestMessage> from(AlertSolving alertSolving) {
    var agentFeatures = alertSolving.getAgentFeatures();
    if (log.isDebugEnabled()) {
      log.debug("Dispatch matches features {} to agents", agentFeatures.keySet());
    }
    // TODO add priority
    return agentFeatures.entrySet().stream().map(entry ->
        AgentExchangeRequestMessage.builder().agentExchangeRequest(
                AgentExchangeRequest
                    .newBuilder()
                    .addAllFeatures(entry.getValue())
                    .addAllMatches(alertSolving.getAllMatchesNames())
                    .build())
            .agentConfig(entry.getKey())
            .requestId(idGenerator.generateId())
            .priority(1).build()
    ).collect(Collectors.toList());
  }
}
