package com.silenteight.serp.common.messaging;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler.DefaultExceptionStrategy;

import javax.validation.ValidationException;

class CustomExceptionStrategy extends DefaultExceptionStrategy {

  @Override
  protected boolean isUserCauseFatal(Throwable cause) {
    // TODO(ahaczewski): Make the list of fatal message processing causes configurable.
    return cause instanceof IllegalArgumentException
        || cause instanceof ValidationException
        || cause instanceof InvalidProtocolBufferException;
  }
}
