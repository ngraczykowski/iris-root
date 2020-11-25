package com.silenteight.serp.governance.bulkchange.integration;

import com.silenteight.proto.serp.v1.governance.ApplyBulkBranchChangeCommand;
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeAppliedEvent;
import com.silenteight.serp.governance.bulkchange.BulkChangeCommands;
import com.silenteight.serp.governance.bulkchange.audit.BulkChangeAuditable;
import com.silenteight.serp.governance.bulkchange.integration.ApplyBulkChangeIntegrationFlowAdapterTest.TestConfiguration;

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

import static com.silenteight.serp.governance.bulkchange.integration.BulkChangeIntegrationModule.APPLY_BULK_CHANGE_OUTBOUND_CHANNEL;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.integration.test.mock.MockIntegration.mockMessageHandler;

@SpringJUnitConfig
@ContextConfiguration(classes = {
    BulkChangeIntegrationFlowAdapterConfiguration.class, TestConfiguration.class })
@SpringIntegrationTest
class ApplyBulkChangeIntegrationFlowAdapterTest {

  private final Fixtures fixtures = new Fixtures();
  @Autowired
  private MessageChannel applyBulkChangeInbound;
  @Autowired
  private ArgumentCaptor<Message<?>> messageArgumentCaptor;
  @MockBean
  private BulkChangeCommands bulkChange;
  @MockBean
  private BulkChangeAuditable bulkChangeAuditable;

  @Test
  void receiveApplyBulkBranchChangeCommand_handle_sendToOutputChannel() {
    // given
    var command = fixtures.applyBulkBranchChangeCommand;
    given(bulkChange.applyBulkBranchChange(command))
        .willReturn(fixtures.bulkBranchChangeAppliedEvent);

    // when
    applyBulkChangeInbound.send(MessageBuilder.withPayload(command).build());

    // then
    verify(bulkChange).applyBulkBranchChange(command);
    verify(bulkChangeAuditable).auditApplication(any(BulkBranchChangeAppliedEvent.class));
    assertThat(messageArgumentCaptor.getAllValues())
        .hasSize(1)
        .extracting(Message::getPayload)
        .extracting(BulkBranchChangeAppliedEvent.class::cast)
        .containsOnly(fixtures.bulkBranchChangeAppliedEvent);
  }

  private static class Fixtures {

    ApplyBulkBranchChangeCommand applyBulkBranchChangeCommand =
        ApplyBulkBranchChangeCommand.newBuilder().build();

    BulkBranchChangeAppliedEvent bulkBranchChangeAppliedEvent =
        BulkBranchChangeAppliedEvent.newBuilder().build();

  }

  @Configuration
  @EnableIntegration
  static class TestConfiguration {

    @SuppressWarnings("UnresolvedMessageChannel")
    @Bean
    @ServiceActivator(inputChannel = APPLY_BULK_CHANGE_OUTBOUND_CHANNEL)
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
