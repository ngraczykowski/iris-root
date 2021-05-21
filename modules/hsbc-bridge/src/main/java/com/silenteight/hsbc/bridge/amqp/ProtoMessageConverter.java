package com.silenteight.hsbc.bridge.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

// TODO:bmartofel fill it with proper implementation
class ProtoMessageConverter extends AbstractMessageConverter {

  @Override
  protected Message createMessage(
      Object object, MessageProperties messageProperties) {
    return new Message("".getBytes(), messageProperties);
  }

  @Override
  public Object fromMessage(Message message) throws MessageConversionException {
    return "";
  }
}
