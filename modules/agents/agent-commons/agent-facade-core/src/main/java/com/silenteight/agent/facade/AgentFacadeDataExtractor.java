package com.silenteight.agent.facade;

import lombok.RequiredArgsConstructor;

import com.silenteight.agent.facade.datasource.AmqpMessageToDataSourceRequestMapper;
import com.silenteight.agent.facade.datasource.DataSourceClient;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

import java.util.List;

import static com.silenteight.agent.facade.GrpcDeadlineHandler.checkRequestTimeout;

@RequiredArgsConstructor
class AgentFacadeDataExtractor<DataSourceRequestT, AgentInputT> {

  private final DataSourceClient<DataSourceRequestT, AgentInputT> dataSourceClient;
  private final AmqpMessageToDataSourceRequestMapper<DataSourceRequestT>
      amqpMessageToDataSourceRequestMapper;

  /**
   * <pre>
   *   Request contains list of matches and list of features which should be used for
   *   (number of matches) x (number of features) calls to the agent
   * </pre>
   *
   * @return inputs from data source
   */
  List<AgentInputT> extract(AgentExchangeRequest request) {
    var dataSourceRequest = amqpMessageToDataSourceRequestMapper.map(request);
    var deadlineTime = request.getDeadlineTime();

    checkRequestTimeout(deadlineTime);
    var matchInputs = dataSourceClient.getMatchInputs(dataSourceRequest);
    checkRequestTimeout(deadlineTime);

    return matchInputs;
  }
}
