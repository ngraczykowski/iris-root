package com.silenteight.serp.governance.bulkchange.integration;

import com.silenteight.proto.serp.v1.governance.BulkBranchChangeRejectedEvent;
import com.silenteight.proto.serp.v1.governance.RejectBulkBranchChangeCommand;
import com.silenteight.serp.governance.bulkchange.BulkChangeCommands;
import com.silenteight.serp.governance.bulkchange.audit.BulkChangeAuditable;
import com.silenteight.serp.governance.bulkchange.integration.RejectBulkChangeIntegrationFlowAdapterTest.TestConfiguration;

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

import static com.silenteight.serp.governance.bulkchange.integration.BulkChangeIntegrationModule.REJECT_BULK_CHANGE_OUTBOUND_CHANNEL;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.integration.test.mock.MockIntegration.mockMessageHandler;

@SpringJUnitConfig
@ContextConfiguration(classes = {
    BulkChangeIntegrationFlowAdapterConfiguration.class, TestConfiguration.class })
@SpringIntegrationTest
class RejectBulkChangeIntegrationFlowAdapterTest {

  private final Fixtures fixtures = new Fixtures();
  @Autowired
  private MessageChannel rejectBulkChangeInbound;
  @Autowired
  private ArgumentCaptor<Message<?>> messageArgumentCaptor;
  @MockBean
  private BulkChangeCommands bulkChange;
  @MockBean
  private BulkChangeAuditable bulkChangeAuditable;

  @Test
  void receiveRejectBulkBranchChangeCommand_handle_sendToOutputChannel() {
    // given
    var command = fixtures.rejectBulkBranchChangeCommand;
    given(bulkChange.rejectBulkBranchChange(command))
        .willReturn(fixtures.bulkBranchChangeRejectedEvent);

    // when
    rejectBulkChangeInbound.send(MessageBuilder.withPayload(command).build());

    // then
    verify(bulkChange).rejectBulkBranchChange(command);
    verify(bulkChangeAuditable).auditRejection(any(BulkBranchChangeRejectedEvent.class));
    assertThat(messageArgumentCaptor.getAllValues())
        .hasSize(1)
        .extracting(Message::getPayload)
        .extracting(BulkBranchChangeRejectedEvent.class::cast)
        .containsOnly(fixtures.bulkBranchChangeRejectedEvent);
  }

  private static class Fixtures {

    RejectBulkBranchChangeCommand rejectBulkBranchChangeCommand =
        RejectBulkBranchChangeCommand.newBuilder().build();

    BulkBranchChangeRejectedEvent bulkBranchChangeRejectedEvent =
        BulkBranchChangeRejectedEvent.newBuilder().build();

  }

  @Configuration
  @EnableIntegration
  static class TestConfiguration {

    @SuppressWarnings("UnresolvedMessageChannel")
    @Bean
    @ServiceActivator(inputChannel = REJECT_BULK_CHANGE_OUTBOUND_CHANNEL)
    MessageHandler handleOutputChannel() {
      return mockMessageHandler(messageArgumentCaptor())
          .handleNext(m -> {
          });
    }

    @Bean
    ArgumentCaptor<Message<?>> messageArgumentCaptor() {
      return MockIntegration.messageArgumentCaptor();
    }
  }
}
