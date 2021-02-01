package com.silenteight.sep.base.common.support.messaging;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePostProcessor;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;

class CompositeAmqpMessagesPostProcessorTest {

  private final Fixtures fixtures = new Fixtures();

  @Test
  void givenTwoPostprocessors_appliesCorrectly() {
    var postProcessors = List.<MessagePostProcessor>of(
        message -> MessageBuilder.fromClonedMessage(message).setHeader("header-1", true).build(),
        message -> MessageBuilder.fromClonedMessage(message).setHeader("header-2", true).build(),
        message -> MessageBuilder.fromClonedMessage(message).setHeader("header-1", false).build()
    );
    var underTest = new CompositeAmqpMessagesPostProcessor(postProcessors);

    var actual = underTest.postProcessMessage(fixtures.message);

    var actualMessageProperties = actual.getMessageProperties();
    then(actual.getBody()).isEqualTo(fixtures.message.getBody());
    then(actualMessageProperties.<Boolean>getHeader("header-1")).isEqualTo(false);
    then(actualMessageProperties.<Boolean>getHeader("header-2")).isEqualTo(true);
  }

  @Test
  void givenNull_throws() {
    ThrowingCallable when = () -> new CompositeAmqpMessagesPostProcessor(null);

    thenNullPointerException().isThrownBy(when);
  }

  @Test
  void giveNoPostprocessors_returnsSameMessage() {
    var underTest = new CompositeAmqpMessagesPostProcessor(emptyList());

    Message actual = underTest.postProcessMessage(fixtures.message);

    then(actual).isEqualTo(fixtures.message);
  }

  private static class Fixtures {

    Message message = MessageBuilder.withBody("messageBody".getBytes()).build();
  }
}
