package com.silenteight.agent.facade;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.agent.facade.datasource.AmqpMessageToDataSourceRequestMapper;
import com.silenteight.agent.facade.datasource.DataSourceClient;
import com.silenteight.agent.facade.exchange.AgentFacadeProperties;
import com.silenteight.agent.monitoring.Monitoring;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.silenteight.agent.facade.GrpcDeadlineHandler.checkRequestTimeout;
import static java.util.stream.Collectors.joining;

@Slf4j
public abstract class AbstractAgentFacade<DataSourceRequestT, AgentInputT extends AgentInput>
    implements AgentFacade<AgentExchangeRequest, AgentExchangeResponse> {

  protected static final String AGENT_ERROR = "AGENT_ERROR";

  private final DataSourceClient<DataSourceRequestT, AgentInputT>
      dataSourceClient;
  private final AmqpMessageToDataSourceRequestMapper<DataSourceRequestT>
      amqpMessageToDataSourceRequestMapper;
  protected final AgentErrorHandler errorHandler;
  private AgentFacadeWorker<AgentInputT> worker =
      new AgentFacadeWorker<>(this::getAgentResponsesForMatch, 1);

  protected AbstractAgentFacade(
      DataSourceClient<DataSourceRequestT, AgentInputT> dataSourceClient,
      AmqpMessageToDataSourceRequestMapper<DataSourceRequestT> amqpMessageToDataSourceRequestMapper,
      Monitoring monitoring) {
    this.dataSourceClient = dataSourceClient;
    this.amqpMessageToDataSourceRequestMapper = amqpMessageToDataSourceRequestMapper;
    this.errorHandler = new AgentErrorHandler(monitoring);
  }

  /**
   * the class is abstract thus dependency injection cannot be done in constructor without changing
   * the implementation of child classes
   * in order to preserve the client code, the injection is done via setter
   */
  @Autowired
  public void configure(AgentFacadeProperties agentFacadeProperties) {
    var parallelism = agentFacadeProperties.getParallelism();
    log.info("Configuring Agent Facade parallelism to {}", parallelism);
    worker = new AgentFacadeWorker<>(this::getAgentResponsesForMatch, parallelism);
  }

  @Override
  public AgentExchangeResponse processMessage(AgentExchangeRequest request) {
    log.info("Received new message: " + request.toString());

    List<AgentInputT> agentsInput;

    try {
      agentsInput = getDataInputsFromDataSource(request);

      logAgentsInputFromDataSource(agentsInput);
    } catch (Exception dataSourceException) {
      log.error("Data Source exception occurred: " + dataSourceException.getMessage(),
          dataSourceException);
      return errorHandler.createErrorResponse(request, dataSourceException);
    }

    List<AgentOutput> agentOutputs = worker.process(agentsInput);

    logAgentOutputs(agentOutputs);

    return AgentExchangeResponse.newBuilder()
        .addAllAgentOutputs(agentOutputs)
        .build();
  }

  private void logAgentOutputs(List<AgentOutput> agentOutputs) {
    var agentOutputsMatches = getAgentOutputsMatches(agentOutputs);
    var agentOutputsFeatures = getAgentOutputsFeatures(agentOutputs);
    log.info(
        "Agent responded, data outputs count: {}, outputs matches: {}, outputs features: {}",
        agentOutputs.size(),
        agentOutputsMatches,
        agentOutputsFeatures
    );
  }

  private String getAgentOutputsMatches(List<AgentOutput> agentOutputs) {
    return agentOutputs.stream()
        .map(AgentOutput::getMatch)
        .collect(joining(","));
  }

  private String getAgentOutputsFeatures(List<AgentOutput> agentOutputs) {
    return agentOutputs.stream()
        .flatMap(output -> output.getFeaturesList().stream())
        .map(Feature::getFeature)
        .collect(joining(","));
  }

  private void logAgentsInputFromDataSource(List<AgentInputT> agentsInput) {
    log.info("Received data inputs from data source with count: " + agentsInput.size());
    String features = getAgentsInputsFeatures(agentsInput);
    String matches = getAgentsInputsMatches(agentsInput);
    log.debug("Received agents inputs from data source with matches {} and features {}",
        matches, features);
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

  protected abstract AgentOutput getAgentResponsesForMatch(AgentInputT agentInput);

  /**
   * <pre>
   *   Request contains list of matches and list of features which should be used for
   *   (number of matches) x (number of features) calls to the agent
   * </pre>
   *
   * @return inputs from data source
   */
  private List<AgentInputT> getDataInputsFromDataSource(AgentExchangeRequest request) {
    DataSourceRequestT dataSourceRequest =
        amqpMessageToDataSourceRequestMapper.map(request);

    var deadlineTime = request.getDeadlineTime();

    checkRequestTimeout(deadlineTime);
    var matchInputs = dataSourceClient.getMatchInputs(dataSourceRequest);
    checkRequestTimeout(deadlineTime);

    return matchInputs;
  }
}
