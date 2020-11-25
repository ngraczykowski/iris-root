package com.silenteight.serp.governance.solutiondiscrepancy;

import com.silenteight.proto.serp.v1.circuitbreaker.SolutionDiscrepancyDetectedEvent;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchesDisabledEvent;
import com.silenteight.serp.governance.solutiondiscrepancy.SolutionDiscrepancyIntegrationFlowAdapterTest.TestConfiguration;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.integration.test.mock.MockIntegration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static com.silenteight.serp.governance.solutiondiscrepancy.SolutionDiscrepancyIntegrationFlowAdapter.SOLUTION_DISCREPANCY_OUTBOUND_CHANNEL;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.integration.test.mock.MockIntegration.mockMessageHandler;

@SpringJUnitConfig
@ContextConfiguration(classes = {
    SolutionDiscrepancyIntegrationFlowAdapterConfiguration.class, TestConfiguration.class })
@SpringIntegrationTest
class SolutionDiscrepancyIntegrationFlowAdapterTest {

  @Autowired
  private MessageChannel solutionDiscrepancyInbound;

  @Autowired
  private ArgumentCaptor<Message<?>> messageArgumentCaptor;

  @MockBean
  private SolutionDiscrepancyHandler solutionDiscrepancyHandler;

  private Fixtures fixtures = new Fixtures();

  @Test
  public void receiveEvent_handle_sendToOutputChannel() {
    var inputEvent = fixtures.solutionDiscrepancyDetectedEvent;
    var message = MessageBuilder.withPayload(inputEvent).build();

    given(solutionDiscrepancyHandler.disableReasoningBranches(inputEvent)).willReturn(
        fixtures.reasoningBranchesDisabledEvent);

    solutionDiscrepancyInbound.send(message);

    verify(solutionDiscrepancyHandler).disableReasoningBranches(inputEvent);
    assertThat(messageArgumentCaptor.getAllValues())
        .hasSize(1)
        .extracting(Message::getPayload)
        .extracting(ReasoningBranchesDisabledEvent.class::cast)
        .containsOnly(fixtures.reasoningBranchesDisabledEvent);
  }

  class Fixtures {

    SolutionDiscrepancyDetectedEvent solutionDiscrepancyDetectedEvent =
        SolutionDiscrepancyDetectedEvent.newBuilder().build();

    ReasoningBranchesDisabledEvent reasoningBranchesDisabledEvent =
        ReasoningBranchesDisabledEvent.newBuilder().build();
  }

  @Configuration
  @EnableIntegration
  public static class TestConfiguration {

    @Bean
    public ArgumentCaptor<Message<?>> messageArgumentCaptor() {
      return MockIntegration.messageArgumentCaptor();
    }

    @Bean
    @ServiceActivator(inputChannel = SOLUTION_DISCREPANCY_OUTBOUND_CHANNEL)
    public MessageHandler handleOutputChannel() {
      return mockMessageHandler(messageArgumentCaptor())
          .handleNext(m -> {
          });
    }
  }
}
