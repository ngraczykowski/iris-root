package com.silenteight.customerbridge.common.messaging;

import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.rabbitmq.client.AMQP.BasicProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Flux.just;

@ExtendWith(MockitoExtension.class)
class ReactiveMessageSenderTest {

  private final Fixtures fixtures = new Fixtures();
  @Mock
  private Sender sender;
  @Mock
  private MessageConverter messageConverter;
  @InjectMocks
  private ReactiveMessageSender underTest;

  @BeforeEach
  void setUp() {
    underTest.setDefaultExchange(Fixtures.EXCHANGE);
    underTest.setDefaultRoutingKey(Fixtures.ROUTING_KEY);
  }

  @Test
  void givenOneMessage_sendsCorrectMessage() {
    var actualSentMessages = mockSenderAndReceiveMessages();
    var testMessage = fixtures.message;
    mockMessageConverter(testMessage);

    StepVerifier
        .create(underTest.send(just(testMessage.asProtoMessage())).log())
        .expectNext()
        .expectComplete()
        .verify();

    then(actualSentMessages)
        .hasSize(1)
        .first(OutboundMessageAssertion.FACTORY)
        .isSameAs(testMessage.asOutboundMessage());
  }

  private void mockMessageConverter(TestMessage... testMessages) {
    List<Message> messages = stream(testMessages)
        .map(TestMessage::asMessage)
        .collect(toList());
    List<Message> allButFirst = messages.subList(1, messages.size());

    given(messageConverter.toMessage(any(), any(MessageProperties.class)))
        .willReturn(messages.get(0), allButFirst.toArray(Message[]::new));
  }

  private List<OutboundMessage> mockSenderAndReceiveMessages() {
    var receivedMessages = new ArrayList<OutboundMessage>();
    given(sender.send(any())).willAnswer(invocation -> {
      Flux<OutboundMessage> flux = invocation.getArgument(0);
      return flux.doOnNext(receivedMessages::add).then();
    });

    return receivedMessages;
  }

  @Test
  void givenTwoMessages_sendsThemInCorrectOrder() {
    var actualSentMessages = mockSenderAndReceiveMessages();
    var firstMessage = fixtures.message;
    var secondMessage = fixtures.otherMessage;
    mockMessageConverter(firstMessage, secondMessage);
    var messagesToSend = just(firstMessage.asProtoMessage(), secondMessage.asProtoMessage());

    StepVerifier
        .create(underTest.send(messagesToSend).log())
        .expectNext()
        .expectNext()
        .expectComplete()
        .verify();

    then(actualSentMessages)
        .first(OutboundMessageAssertion.FACTORY)
        .isSameAs(firstMessage.asOutboundMessage());
    then(actualSentMessages)
        .last(OutboundMessageAssertion.FACTORY)
        .isSameAs(secondMessage.asOutboundMessage());
  }

  @Test
  void givenPostProcessorSettingHeaderValue_sendsMessageWithTheHeaderSet() {
    var testMessage = fixtures.message;
    mockMessageConverter(testMessage);
    var actualSentMessages = mockSenderAndReceiveMessages();
    underTest.setPostProcessor(
        message -> MessageBuilder.fromMessage(message).setHeader("header-x", true).build());

    StepVerifier
        .create(underTest.send(just(testMessage.asProtoMessage())).log())
        .expectNext()
        .expectComplete()
        .verify();

    then(actualSentMessages)
        .hasSize(1)
        .first(OutboundMessageAssertion.FACTORY)
        .hasHeaderWithValue("header-x", true);
  }

  private static class Fixtures {

    static final String EXCHANGE = "test-exchange";
    static final String ROUTING_KEY = "test-routingKey";
    final TestMessage message = new TestMessage("message");
    final TestMessage otherMessage = new TestMessage("otherMessage");
  }

  private static final class TestMessage {

    private static final MessagePropertiesConverter CONVERTER =
        new DefaultMessagePropertiesConverter();
    private final Message message;

    private TestMessage(String seed) {
      this.message = MessageBuilder.withBody(seed.getBytes(StandardCharsets.UTF_8)).build();
    }

    OutboundProtoMessage asProtoMessage() {
      BytesValue messageBody = BytesValue.newBuilder()
          .setValue(ByteString.copyFrom(message.getBody()))
          .build();

      return new OutboundProtoMessage(Fixtures.EXCHANGE, Fixtures.ROUTING_KEY, messageBody, 1);
    }

    OutboundMessage asOutboundMessage() {
      return new OutboundMessage(
          Fixtures.EXCHANGE, Fixtures.ROUTING_KEY, getBasicProperties(), asMessage().getBody());
    }

    Message asMessage() {
      return message;
    }

    BasicProperties getBasicProperties() {
      return CONVERTER.fromMessageProperties(getProperties(), "UTF-8");
    }

    MessageProperties getProperties() {
      return message.getMessageProperties();
    }
  }
}
