package com.silenteight.customerbridge.common.messaging;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import reactor.rabbitmq.OutboundMessage;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("UnusedReturnValue")
class OutboundMessageAssertion extends AbstractAssert<OutboundMessageAssertion, OutboundMessage> {

  static final InstanceOfAssertFactory<OutboundMessage, OutboundMessageAssertion> FACTORY =
      new InstanceOfAssertFactory<>(
          OutboundMessage.class, OutboundMessageAssertion::assertThatOutboundMessage);

  private OutboundMessageAssertion(OutboundMessage outboundMessage) {
    super(outboundMessage, OutboundMessageAssertion.class);
  }

  public static OutboundMessageAssertion assertThatOutboundMessage(OutboundMessage message) {
    return new OutboundMessageAssertion(message);
  }

  OutboundMessageAssertion isSameAs(OutboundMessage expected) {
    assertThat(actual.getBody()).isEqualTo(expected.getBody());
    assertThat(actual.getExchange()).isEqualTo(expected.getExchange());
    assertThat(actual.getProperties()).isEqualTo(expected.getProperties());
    assertThat(actual.getRoutingKey()).isEqualTo(expected.getRoutingKey());

    return this;
  }

  OutboundMessageAssertion hasHeaderWithValue(String header, Object value) {
    assertThat(actual.getProperties()).isNotNull();
    assertThat(actual.getProperties().getHeaders()).containsEntry(header, value);

    return this;
  }
}
