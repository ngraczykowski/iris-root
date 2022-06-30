package com.silenteight.agent.facade;

import com.silenteight.agent.facade.datasource.AmqpMessageToDataSourceRequestMapper;
import com.silenteight.agent.facade.datasource.DataSourceClient;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentFacadeDataExtractorTest {

  @Mock
  private DataSourceClient<Object, Object> dataSourceClient;
  @Mock
  private AmqpMessageToDataSourceRequestMapper<Object> mapper;

  @Test
  void shouldExtractData() {
    var request = AgentExchangeRequest.newBuilder().build();
    var underTest = new AgentFacadeDataExtractor<>(dataSourceClient, mapper);
    var dsRequest = new Object();
    var dsResponse = new Object();
    doReturn(dsRequest).when(mapper).map(eq(request));
    doReturn(List.of(dsResponse)).when(dataSourceClient).getMatchInputs(eq(dsRequest));

    var result = underTest.extract(request);

    verify(mapper).map(request);
    verify(dataSourceClient).getMatchInputs(dsRequest);
    assertEquals(result.get(0), dsResponse);
  }
}
