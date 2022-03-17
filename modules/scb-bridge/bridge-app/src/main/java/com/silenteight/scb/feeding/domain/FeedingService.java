package com.silenteight.scb.feeding.domain;

import com.silenteight.scb.feeding.domain.agentinput.AgentInput;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedingService {

  private final List<AgentInput> agentInputs;

  FeedingService(List<AgentInput> agentInputs) {
    if (agentInputs.isEmpty()) {
      throw new IllegalStateException("There are no agent inputs.");
    }
    this.agentInputs = agentInputs;
  }

  List<AgentInputIn<Feature>> createAgentInputIns(Alert alert, Match match) {
    return agentInputs.stream()
        .map(agentInput -> agentInput.createAgentInput(alert, match))
        .toList();
  }
}
