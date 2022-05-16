package com.silenteight.agent.facade;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;

import java.util.List;
import java.util.function.Function;

import static com.silenteight.agents.logging.AgentLogger.debug;
import static com.silenteight.agents.logging.AgentLogger.info;
import static java.util.stream.Collectors.joining;

@Slf4j
class AgentFacadeProcessor<AgentInputT extends AgentInput> {

  private final AgentFacadeWorker<AgentInputT> worker;

  AgentFacadeProcessor(Function<AgentInputT, AgentOutput> processingFunction) {
    worker = new AgentFacadeWorker<>(processingFunction);
  }

  void setParallelism(int parallelism) {
    worker.setParallelism(parallelism);
  }

  private static String getAgentOutputsMatches(List<AgentOutput> agentOutputs) {
    return agentOutputs.stream()
        .map(AgentOutput::getMatch)
        .collect(joining(","));
  }

  private void logAgentOutputs(List<AgentOutput> agentOutputs) {
    var agentOutputsMatches = getAgentOutputsMatches(agentOutputs);
    var agentOutputsFeatures = getAgentOutputsFeatures(agentOutputs);
    info(log, "Agent responded, data outputs count: {}, outputs matches: {}, outputs features: {}",
        agentOutputs::size,
        () -> agentOutputsMatches,
        () -> agentOutputsFeatures);
  }

  private static String getAgentOutputsFeatures(List<AgentOutput> agentOutputs) {
    return agentOutputs.stream()
        .flatMap(output -> output.getFeaturesList().stream())
        .map(Feature::getFeature)
        .collect(joining(","));
  }

  public AgentExchangeResponse process(List<AgentInputT> agentsInput) {

    logAgentsInputFromDataSource(agentsInput);

    List<AgentOutput> agentOutputs = worker.process(agentsInput);

    logAgentOutputs(agentOutputs);

    return AgentExchangeResponse.newBuilder()
        .addAllAgentOutputs(agentOutputs)
        .build();
  }

  private void logAgentsInputFromDataSource(List<AgentInputT> agentsInput) {
    log.info("Received data inputs from data source with count: " + agentsInput.size());
    debug(log, "Received agents inputs from data source with matches {} and features {}",
        () -> getAgentsInputsFeatures(agentsInput), () -> getAgentsInputsMatches(agentsInput));
  }

  private String getAgentsInputsFeatures(List<AgentInputT> agentsInput) {
    return agentsInput
        .stream()
        .map(AgentInput::getFeatureInputsAsString)
        .collect(joining(","));
  }

  private String getAgentsInputsMatches(List<AgentInputT> agentsInput) {
    return agentsInput.stream()
        .map(AgentInput::getMatch)
        .collect(joining(","));
  }
}
