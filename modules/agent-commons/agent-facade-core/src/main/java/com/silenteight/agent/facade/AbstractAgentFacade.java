package com.silenteight.agent.facade;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.agent.facade.datasource.AmqpMessageToDataSourceRequestMapper;
import com.silenteight.agent.facade.datasource.DataSourceClient;
import com.silenteight.agent.facade.exchange.AgentFacadeProperties;
import com.silenteight.agent.monitoring.Monitoring;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractAgentFacade<DataSourceRequestT, AgentInputT extends AgentInput>
    implements AgentFacadeForMatch<AgentInputT> {

  protected final AgentErrorHandler errorHandler;
  private final AgentFacadeProcessor<AgentInputT> processor;
  private final AgentFacadeDataExtractor<DataSourceRequestT, AgentInputT> dataExtractor;

  protected AbstractAgentFacade(
      DataSourceClient<DataSourceRequestT, AgentInputT> dataSourceClient,
      AmqpMessageToDataSourceRequestMapper<DataSourceRequestT> amqpRequestMapper,
      Monitoring monitoring) {
    this.errorHandler = new AgentErrorHandler(monitoring);
    this.processor = new AgentFacadeProcessor<>(this::getAgentResponsesForMatch);
    this.dataExtractor = new AgentFacadeDataExtractor<>(dataSourceClient, amqpRequestMapper);
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
    processor.setParallelism(parallelism);
  }

  @Override
  public AgentExchangeResponse processMessage(AgentExchangeRequest request) {
    log.info("Received new message: " + request.toString());

    try {
      var agentsInput = dataExtractor.extract(request);
      return processor.process(agentsInput);
    } catch (Exception dataSourceException) {
      log.error(
          "Data Source exception occurred: " + dataSourceException.getMessage(),
          dataSourceException);
      return errorHandler.createErrorResponse(request, dataSourceException);
    }
  }
}
