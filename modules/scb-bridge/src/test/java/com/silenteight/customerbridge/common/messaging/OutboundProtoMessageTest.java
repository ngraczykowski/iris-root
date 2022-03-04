package com.silenteight.customerbridge.common.messaging;

import com.silenteight.customerbridge.common.messaging.OutboundProtoMessage.OutboundProtoMessageBuilder;

import com.google.protobuf.Empty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OutboundProtoMessageTest {

  @Test
  void createDefault() {
    var message = givenMessageBuilder().build();

    assertThat(message.getExchange()).isEmpty();
    assertThat(message.getRoutingKey()).isEmpty();
    assertThat(message.getPriority()).isEqualTo(1);
  }

  private OutboundProtoMessageBuilder givenMessageBuilder() {
    return OutboundProtoMessage.builder().message(Empty.getDefaultInstance());
  }

  @Test
  void createWithPriority() {
    var message = givenMessageBuilder().priority(123).build();

    assertThat(message.getPriority()).isEqualTo(123);
  }

  @Test
  void createWithExchangeAndRouting() {
    var message = givenMessageBuilder().exchange("x").routingKey("r").build();

    assertThat(message.getExchange()).hasValue("x");
    assertThat(message.getRoutingKey()).hasValue("r");
  }
}
