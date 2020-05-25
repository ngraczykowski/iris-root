package com.silenteight.serp.common.messaging;

import com.google.protobuf.InvalidProtocolBufferException;
import org.assertj.core.api.AbstractBooleanAssert;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nonnull;
import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.*;

class CustomExceptionStrategyTest {

  private final CustomExceptionStrategy underTest = new CustomExceptionStrategy();

  /**
   * Invalid message causes fatal exceptions, hence there is no easy way around, and requeuing
   * the message will result in a redelivery of those messages. Therefore, we consider
   * validation, Protocol Buffers and argument exceptions as fatal, breaking the cycle.
   */
  @Test
  void fatalExceptions() {
    assertThatFatalExceptionCheck(new IllegalArgumentException()).isTrue();
    assertThatFatalExceptionCheck(new ValidationException()).isTrue();
    assertThatFatalExceptionCheck(new InvalidProtocolBufferException("really bad")).isTrue();
  }

  @Test
  void nonFatalExceptions() {
    assertThatFatalExceptionCheck(new NullPointerException()).isFalse();
    assertThatFatalExceptionCheck(new IllegalStateException()).isFalse();
    assertThatFatalExceptionCheck(new IOException()).isFalse();
    assertThatFatalExceptionCheck(new Exception()).isFalse();
  }

  @Nonnull
  private AbstractBooleanAssert<?> assertThatFatalExceptionCheck(Throwable exception) {
    Message dummyMessage = MessageBuilder
        .withBody("message".getBytes(StandardCharsets.ISO_8859_1))
        .build();

    ListenerExecutionFailedException listenerException =
        new ListenerExecutionFailedException("failed", exception, dummyMessage);

    return assertThat(underTest.isFatal(listenerException));
  }
}
